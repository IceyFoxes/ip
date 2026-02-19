package icey.command;

import icey.IceyException;
import icey.Storage;
import icey.task.Task;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        tasks.validateIndex(index);
        Task task = tasks.get(index);
        if (task.isDone()) {
            throw new IceyException("Task is already marked as done.");
        }
        task.markAsDone();
        storage.save(tasks);
        ui.showMessages("Caught and completed:", ui.getIndent() + task.toString());
    }
}
