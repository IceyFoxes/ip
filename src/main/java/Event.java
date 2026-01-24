/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {
    protected String from;
    protected String by;

    /**
     * Creates a new Event task with the specified description and time period.
     *
     * @param description The description of the event.
     * @param from        The start time of the event.
     * @param by          The end time of the event.
     */
    public Event(String description, String from, String by) {
        super(TaskType.EVENT, description);
        this.from = from;
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + by + ")";
    }

}
