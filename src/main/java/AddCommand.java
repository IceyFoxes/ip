/**
 * Command to add a task (todo, deadline, or event).
 */
public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        tasks.add(task);
        storage.save(tasks);
        int pending = tasks.countPending();
        ui.showMessages("I've added Task:", ui.getIndent() + task.toString(),
                tasks.getSize() + " tasks (" + pending + " pending) in the list.");
    }
}
