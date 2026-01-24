/**
 * Represents a task that needs to be completed by a specific deadline.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Creates a new Deadline task with the specified description and due date.
     *
     * @param description The description of the deadline task.
     * @param by          The deadline by which the task should be completed.
     */
    public Deadline(String description, String by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    public String getBy() {
        return this.by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
