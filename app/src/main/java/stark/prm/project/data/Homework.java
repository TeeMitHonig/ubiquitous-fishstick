package stark.prm.project.data;

import java.util.Date;

public class Homework extends Note {

    private int pageNumber;
    private double progress; // progress of homework in %
    private Date dueDate;
    private Module module; // always has to match up with the module of inherited field 'lecture', if said field is not null

    public Homework(String description, Module module, int pageNumber, double progress, Date dueDate) {
        super(description, null);
        this.pageNumber = pageNumber;
        this.progress = progress;
        this.dueDate = dueDate;
        this.module = module;
    }

    /**
     * The module for this homework is determined implicitly by the passed lecture.
     * Hence, this constructor does not accept the passed lecture to be null.
     * If it is, use the other Constructor {@link Homework#Homework(String, Module, int, double, Date)} instead.
     *
     * @param description
     * @param lecture
     * @param pageNumber
     * @param progress
     * @param dueDate
     */
    public Homework(String description, Lecture lecture, int pageNumber, double progress, Date dueDate) {
        super(description, lecture);
        this.pageNumber = pageNumber;
        this.progress = progress;
        this.dueDate = dueDate;
        if (lecture == null)
            throw new IllegalArgumentException("lecture cannot be null");
        this.module = lecture.getModule();
    }



    //----------------------------------------------------------------------------------------------


    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        if(this.getLecture() != null) {
            if(!this.getLecture().getModule().equals(module))
                throw new IllegalArgumentException("provided module does not match module of lecture");
        }
        this.module = module;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
