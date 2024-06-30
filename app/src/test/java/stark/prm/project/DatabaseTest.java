package stark.prm.project;

import org.junit.Test;
import org.junit.Assert;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import stark.prm.project.data.Database;
import stark.prm.project.data.Homework;
import stark.prm.project.data.Lecture;
import stark.prm.project.data.Module;
import stark.prm.project.data.Note;

public class DatabaseTest {

    /**
     * GIVEN a database
     * WHEN a module, a lecture and a note is added,
     * THEN the database holds one module, one lecture and one note.
     */
    @Test
    public void testDbAdd() {
        Database db = Database.getInstance();
        Module mod1 = new Module("module 1", "Dozent 1");
        Lecture lec1 = new Lecture(mod1, "topic 1");

        db.add(mod1);
        db.add(lec1);
        db.add(new Note("description 1", lec1));


        Assert.assertEquals(1, db.getModules().size());
        Assert.assertEquals(1, db.getLectures().size());
        Assert.assertEquals(1, db.getLectures().size());

        db.destroy();
    }

    /**
     * Yeah I'm sorry this test is complete spaghetti code but I just wanted to get it over with lol
     */
    @Test
    public void addHomeworkTest() {
        Database db = Database.getInstance();
        Module mod1 = new Module("module 1", "Dozent 1");
        Lecture lec1 = new Lecture(mod1, "topic 1");


        final Homework HOMEWORK_WITH_LECTURE = new Homework(
                "homework with lecture",
                lec1,
                2,
                .5,
                new Date()
        );
        final Homework HOMEWORK_WITH_MODULE = new Homework(
                "homework with module",
                mod1,
                2,
                .5,
                new Date()
        );
        db.add(HOMEWORK_WITH_LECTURE);
        db.add(HOMEWORK_WITH_MODULE);

        // Test overwriting of module
        boolean errorThrown = false;
        try {
            HOMEWORK_WITH_LECTURE.setModule(
                    new Module(
                            "another module",
                            "another dozent"
                    )
            );
        } catch (IllegalArgumentException e) {
            errorThrown = true;
        }

        Assert.assertTrue(errorThrown);


        Assert.assertEquals(2, db.getNotes().size());
        Assert.assertNotNull(db.getNotes().get(HOMEWORK_WITH_LECTURE.getId())); // check that module is transitively accessible

        db.destroy();
    }

    /**
     * GIVEN a database with a lecture L and 2 notes referring to L and 1 note not referring to L
     * WHEN {@link Lecture#getNotes()} is called to find all notes referring to L
     * THEN the 2 notes referring to L should be returned.
     */
    @Test
    public void getNotesFromLecture() {
        Database db = Database.getInstance();
        Module tempModule = new Module("module 2 + 3i", "Hellmich");
        Lecture testLecture = new Lecture(
                tempModule,
                "complex numbers"
        );
        db.add(testLecture);

        db.add(new Note("note 1", testLecture));
        db.add(new Note("note 2", testLecture));
        db.add(new Note("note 3", new Lecture(
                tempModule,
                "Note 3 should not be referenced"
        )));

        final List<Note> NOTES_OF_TEST_LECTURE = testLecture.getNotes();

        Assert.assertEquals(2, NOTES_OF_TEST_LECTURE.size());
        // I'm also sorry for this, but generating a hash map of ID's (which will mess wiith the order of the created objects)
        // makes this the only sure-fire way to verify a certain element exists in the List
        // I also could have stored an Object reference when I created the object and used List::contains but naaaaaaaaaaaaaaaaaaaaah
        Assert.assertEquals(1, NOTES_OF_TEST_LECTURE.stream().map(Note::getDescription).filter(d -> d.equals("note 1")).count());
        Assert.assertEquals(1, NOTES_OF_TEST_LECTURE.stream().map(Note::getDescription).filter(d -> d.equals("note 2")).count());

        //Check for non-existance of third Note
        Assert.assertFalse(NOTES_OF_TEST_LECTURE.stream().map(Note::getDescription).filter(d -> d.equals("note 3")).anyMatch(m -> true));


        db.destroy();
    }

    /**
     * GIVEN a database with a module M and 2 notes referring to M and 1 note not referring to M
     * WHEN {@link Module#getNotes()} is called to find all notes referring to M
     * THEN the 2 notes referring to M should be returned.
     */
    @Test
    public void getNotesFromModule() {
        Database db = Database.getInstance();
        Module testModule = new Module("module 1", "Dozent 1");
        db.add(testModule);

        db.add(new Homework(
                "homework 1",
                testModule,
                2,
                .5,
                new Date()
        ));
        db.add(new Homework(
                "homework 2",
                testModule,
                2,
                .5,
                new Date()
        ));
        db.add(new Homework(
                "homework 3",
                new Module(
                        "another module",
                        "another dozent"
                ),
                2,
                .5,
                new Date()
        ));

        final List<Note> NOTES_OF_TEST_MODULE = testModule.getNotes();

        Assert.assertEquals(2, NOTES_OF_TEST_MODULE.size());
        // I'm also sorry for this, but generating a hash map of ID's (which will mess wiith the order of the created objects)
        // makes this the only sure-fire way to verify a certain element exists in the List
        // I also could have stored an Object reference when I created the object and used List::contains but naaaaaaaaaaaaaaaaaaaaah
        Assert.assertEquals(1, NOTES_OF_TEST_MODULE.stream().map(Note::getDescription).filter(d -> d.equals("homework 1")).count());
        Assert.assertEquals(1, NOTES_OF_TEST_MODULE.stream().map(Note::getDescription).filter(d -> d.equals("homework 2")).count());

        //Check for non-existance of third Note
        Assert.assertFalse(NOTES_OF_TEST_MODULE.stream().map(Note::getDescription).filter(d -> d.equals("homework 3")).anyMatch(m -> true));

        db.destroy();
    }


    @Test
    public void getModuleByNameTest() {
        Database db = Database.getInstance();

        db.add(new Module("name", "dozent"));
        db.add(new Module("another name", "dozent"));


        db.destroy();
    }
}
