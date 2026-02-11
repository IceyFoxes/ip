package icey.task;

import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a description and completion status. Serves as the
 * base class for specific task types like Todo, Deadline, and Event.
 */
public class Task {
    public static final String DONE_SYMBOL = "1";
    public static final String NOT_DONE_SYMBOL = "0";
    protected static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");
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

    public boolean isDone() {
        return this.isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public TaskType getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a formatted string for saving this task to storage.
     *
     * @param delimiter The delimiter to use between fields.
     * @param dateFormat The date format for any date fields.
     * @return The formatted storage string.
     */
    public String toStorageString(String delimiter, DateTimeFormatter dateFormat) {
        String done = isDone ? DONE_SYMBOL : NOT_DONE_SYMBOL;
        return type.getSymbol() + delimiter + done + delimiter + description;
    }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
