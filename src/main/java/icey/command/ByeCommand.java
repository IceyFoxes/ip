package icey.command;

import icey.Storage;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to exit the application.
 */
public class ByeCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
