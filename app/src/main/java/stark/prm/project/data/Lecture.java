package stark.prm.project.data;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Lecture {
    private UUID id;
    private Module module;

    private String topic;

    //TODO add List of Notes?

    public Lecture(Module module, String topic) {
        this.id = UUID.randomUUID();
        if(module == null)
            throw new IllegalArgumentException("module cannot be null");
        if(Database.getInstance().getLectureByTopic(topic) != null)
            throw new IllegalArgumentException("Lecture.topic must be unique");

        this.module = module;
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture)) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id) && Objects.equals(module, lecture.module) && Objects.equals(topic, lecture.topic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, module, topic);
    }

    /**
     * Get all notes referring to this instance of Lecture.
     * Currently realized through List comprehension, because I didn't want to create redundant data
     * in keeping a list of Notes around for each Lecture.
     *
     * @return List of notes
     */
    public List<Note> getNotes() {
        return Database.getInstance().getNotes().values().stream()
                .filter(n -> n.getLecture().equals(this))
                .collect(Collectors.toList());
    }


    //----------------------------------------------------------------------------------------------

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        if(module == null)
            throw new IllegalArgumentException("module cannot be null");
        this.module = module;
    }
}
