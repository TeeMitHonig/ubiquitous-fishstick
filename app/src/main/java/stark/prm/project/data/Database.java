package stark.prm.project.data;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
                .filter(m -> m.getName().contains(target))
                .findFirst()
                .orElse(null);
    }

    public Lecture getLectureByTopic(String target) {
        return (Lecture) lectures.values().stream()
                .filter(m -> m.getTopic().contains(target))
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
