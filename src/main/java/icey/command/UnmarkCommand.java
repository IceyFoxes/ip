package icey.command;

import icey.IceyException;
import icey.Storage;
import icey.task.Task;
import icey.task.TaskList;
import icey.ui.Ui;

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
        tasks.validateIndex(index);
        Task task = tasks.get(index);
        if (!task.isDone()) {
            throw new IceyException("Task is already marked as not done.");
        }
        task.markAsNotDone();
        storage.save(tasks);
        ui.showMessages("Task back on the hunt:", ui.getIndent() + task.toString());
    }
}
