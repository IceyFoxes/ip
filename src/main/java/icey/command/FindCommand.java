package icey.command;

import java.util.ArrayList;

import icey.IceyException;
import icey.Storage;
import icey.task.Task;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to find tasks containing a keyword (case-insensitive).
 */
public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        ArrayList<String> matches = new ArrayList<>();
        for (int i = 0; i < tasks.getSize(); i++) {
            Task task = tasks.get(i);
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matches.add((i + 1) + "." + task.toString());
            }
        }

        if (matches.isEmpty()) {
            ui.showMessages("No matching tasks found.");
        } else {
            String[] output = new String[matches.size() + 1];
            output[0] = "Here are the matching tasks in your list:";
            for (int i = 0; i < matches.size(); i++) {
                output[i + 1] = matches.get(i);
            }
            ui.showMessages(output);
        }
    }
}
