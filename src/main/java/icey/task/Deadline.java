package icey.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that needs to be completed by a specific deadline.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task with the specified description and due date.
     *
     * @param description The description of the deadline task.
     * @param by          The deadline by which the task should be completed.
     */
    public Deadline(String description, LocalDateTime by) {
        super(TaskType.DEADLINE, description);
        this.by = by;
    }

    public LocalDateTime getBy() {
        return this.by;
    }

    @Override
    public String toStorageString(String delimiter, DateTimeFormatter dateFormat) {
        return super.toStorageString(delimiter, dateFormat) + delimiter + by.format(dateFormat);
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
    }
}
