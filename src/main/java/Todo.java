/**
 * Represents a simple task without any time constraints.
 */
public class Todo extends Task {
    /**
     * Creates a new Todo task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(TaskType.TODO, description);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
