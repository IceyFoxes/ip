package icey.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates a new Event task with the specified description and time period.
     *
     * @param description The description of the event.
     * @param from        The start time of the event.
     * @param to          The end time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    @Override
    public String toStorageString(String delimiter, DateTimeFormatter dateFormat) {
        return super.toStorageString(delimiter, dateFormat)
                + delimiter + from.format(dateFormat) + delimiter + to.format(dateFormat);
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from.format(OUTPUT_FORMAT) + " to: " + to.format(OUTPUT_FORMAT)
                + ")";
    }
}
