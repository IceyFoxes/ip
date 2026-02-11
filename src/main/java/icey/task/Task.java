package icey.task;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    protected ArrayList<String> tags = new ArrayList<>();

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
     * Adds a tag to this task if it doesn't already exist.
     *
     * @param tag The tag to add.
     * @return true if the tag was added, false if it already exists.
     */
    public boolean addTag(String tag) {
        if (tags.contains(tag)) {
            return false;
        }
        tags.add(tag);
        return true;
    }

    public ArrayList<String> getTags() {
        return tags;
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
        String tagsStr = tags.isEmpty() ? "" : " " + String.join(" ", tags);
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description + tagsStr;
    }
}
