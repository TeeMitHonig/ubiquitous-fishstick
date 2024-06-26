package stark.prm.project.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import android.content.Context;

public class Database {
    private static Database instance;
    private static HashMap<UUID, Lecture> lectures;
    private static HashMap<UUID, Module> modules;
    // Since Homeworks are Notes, they should also be stored in this Map.
    private static HashMap<UUID, Note> notes;


    private Database() {
        lectures = new HashMap<>();
        modules = new HashMap<>();
        notes = new HashMap<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void destroy() {
        instance = null;
    }

    /**
     * Initialize the Database with mockup Modules.
     * Any further Data will have to either be added via the app or put here, in <i>its own</i> if-statement.
     *
     * <em>Can only be called on a {@link Database} object, not the Class!!!</em>
     *
     *
     * @param context
     */
    public void init(Context context) {
        this.load(context);
        if(modules.size() < 3) {
            modules.clear();
            this.add(new Module("PRM", "Wolfgang Stark"));
            this.add(new Module("Web Engineering", "Ivan Bogicevic"));
            this.add(new Module("Analysis", "Jürgen Hellmich"));
            this.commit(context); // write mockup data to files, so that we save overhead
        }
    }


    /**
     * Writes the current content of the HashMaps to a csv file.
     * Call this whenever you are done adding/changing data and continue to another state in your application.
     * <p>
     * uses {@link CSVHandler}.
     */
    public void commit(Context context) {
        CSVHandler.writeLectures(context, lectures);
        CSVHandler.writeModules(context, modules);

        // Filter out Homeworks from the notes Map and write them separately, as in this specific use case it outright won't work treating them the same.
        CSVHandler.writeNotes(context, notes.values()
                .stream()
                .filter(n -> !(n instanceof Homework))
                .collect(Collectors.toMap(Note::getId, n -> n)));

        CSVHandler.writeHomeworks(context, notes.values().stream()
                .filter(n -> n instanceof Homework)
                .map(n -> (Homework) n)
                .collect(Collectors.toMap(Homework::getId, n -> n)));
    }

    /**
     * Reads database entries from their respective CSV files.
     * <br>
     * Note that the order in which we read them is important, as objects refer to one another.
     * If e.g. Notes are read before Lectures, on creation they expect to find a {@link Lecture} object
     * with the ID stored in the database, however when {@link Database#lectures} is empty, this will fail.
     *<br>
     * The correct order must be: Modules -> Lectures -> Notes -> Homeworks
     */
    public void load(Context context) {
        modules = CSVHandler.readModules(context, this);
        lectures = CSVHandler.readLectures(context, this);
        notes = CSVHandler.readNotes(context, this);
        notes.putAll(CSVHandler.readHomeworks(context, this));
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Adds a note to the database.
     * Key for the {@link HashMap} is the {@link Note#getId()} of the note.
     *
     * @param note note to add
     */
    public void add(Note note) {
        notes.put(note.getId(), note);
    }

    /**
     * Adds a lecture to the database.
     * Key for the {@link HashMap} is the {@link Lecture#getId()} of the lecture.
     *
     * @param lecture lecture to add
     */
    public void add(Lecture lecture) {
        lectures.put(lecture.getId(), lecture);
    }

    /**
     * Adds a module to the database.
     * Key for the {@link HashMap} is the {@link Module#getId()} of the module.
     *
     * @param module module to add
     */
    public void add(Module module) {
        modules.put(module.getId(), module);
    }


    /**
     * Updates a note in the database.
     * Determines which note to update by {@link Note#getId()} of the passed note.
     *
     * @param note stand-alone {@link Note} object with respective changes
     * @return note if replacement was successful, else null
     */
    public Note update(Note note) {
        return notes.replace(note.getId(), note);
    }

    /**
     * Updates a module in the database.
     * Determines which module to update by {@link Module#getId()} of the passed Module.
     *
     * @param module stand-alone {@link Module} object with respective changes
     * @return module if replacement was successful, else null
     */
    public Module update(Module module) {
        return modules.replace(module.getId(), module);
    }

    /**
     * Updates a lecture in the database.
     * Determines which lecture to add by {@link Lecture#getId()} of the passed lecture.
     *
     * @param lecture stand-alone {@link Lecture} object with respective changes
     * @return lecture if replacement was successful, else null
     */
    public Lecture update(Lecture lecture) {
        return lectures.replace(lecture.getId(), lecture);
    }


    //TODO figure this out if I'm feeling masochistic at some time
//    public Homework getHomeworkBy(AttributeOp aop, Object expected) {
//        return (Homework) notes.values().stream().filter(m -> m instanceof Homework)
//                .map(aop::getAttribute)
//                .filter(m -> m.equals(expected))
//                .findFirst()
//                .orElse(null);
//    }

//    public interface AttributeOp {
//        <T> T getAttribute(Object obj);
//    }


    public Module getModuleByName(String target) {
        return (Module) modules.values().stream()
                .filter(m -> m.getName().equals(target))
                .findFirst()
                .orElse(null);
    }

    public Lecture getLectureByTopic(String target) {
        return (Lecture) lectures.values().stream()
                .filter(m -> m.getTopic().equals(target))
                .findFirst()
                .orElse(null);

    }


    //----------------------------------------------------------------------------------------------

    public void setInstance(Database instance) {
        Database.instance = instance;
    }

    public HashMap<UUID, Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(HashMap<UUID, Lecture> lectures) {
        Database.lectures = lectures;
    }

    public HashMap<UUID, Module> getModules() {
        return modules;
    }

    public void setModules(HashMap<UUID, Module> modules) {
        Database.modules = modules;
    }

    public HashMap<UUID, Note> getNotes() {
        return notes;
    }

    public void setNotes(HashMap<UUID, Note> notes) {
        Database.notes = notes;
    }
}
