public class Event extends Task {
    protected String from;
    protected String by;

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
