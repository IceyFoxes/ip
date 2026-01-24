package icey.command;

import icey.IceyException;
import icey.Storage;
import icey.task.Task;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private final int index;

    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        if (index < 0 || index >= tasks.getSize()) {
            throw new IceyException("Invalid task number.");
        }
        Task task = tasks.remove(index);
        storage.save(tasks);
        int pending = tasks.countPending();
        ui.showMessages("I've removed this task:", ui.getIndent() + task.toString(),
                tasks.getSize() + " tasks (" + pending + " pending) in the list.");
    }
}
