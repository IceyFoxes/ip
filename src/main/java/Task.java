/**
 * Represents a task with a description and completion status.
 * Serves as the base class for specific task types like Todo, Deadline, and Event.
 */
public class Task {
    protected TaskType type;
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new task with the specified type and description.
     *
     * @param type        The type of task.
     * @param description The description of the task.
     */
    public Task(TaskType type, String description) {
        this.type = type;
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public Boolean isDone() {
        return this.isDone;
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
