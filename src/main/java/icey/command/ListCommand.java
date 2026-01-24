package icey.command;

import icey.Storage;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to list all tasks.
 */
public class ListCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        if (tasks.isEmpty()) {
            ui.showMessages("No tasks yet!");
            return;
        }

        String[] taskLines = new String[tasks.getSize() + 1];
        taskLines[0] = "Here are the tasks:";

        for (int i = 0; i < tasks.getSize(); i++) {
            taskLines[i + 1] = ui.getIndent() + (i + 1) + ". " + tasks.get(i);
        }

        ui.showMessages(taskLines);
    }
}
