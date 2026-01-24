/**
 * Command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final int index;

    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        if (index < 0 || index >= tasks.getSize()) {
            throw new IceyException("Invalid task number.");
        }
        Task task = tasks.get(index);
        if (!task.isDone()) {
            throw new IceyException("Task is already marked as not done.");
        }
        task.isDone = false;
        storage.save(tasks);
        ui.showMessages("Task marked as not done:", ui.getIndent() + task.toString());
    }
}
