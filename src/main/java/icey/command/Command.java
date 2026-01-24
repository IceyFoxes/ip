package icey.command;

import icey.IceyException;
import icey.Storage;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Abstract base class for all commands.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI for displaying output.
     * @param storage The storage for saving tasks.
     * @throws IceyException If an error occurs during execution.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException;

    /**
     * Returns whether this command should exit the application.
     *
     * @return true if the application should exit, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
