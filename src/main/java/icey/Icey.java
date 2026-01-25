package icey;

import icey.command.Command;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Represents the main chatbot application that manages tasks. Supports
 * creating, listing, marking, and deleting tasks through a command-line
 * interface.
 */
public class Icey {
    private static final String NAME = "Icey";
    private static final String DATA_PATH = "data/icey.txt";
    private final Ui ui = new Ui();
    private final Storage storage = new Storage(DATA_PATH);
    private TaskList tasks;

    /**
     * Starts the chatbot and processes user commands in a loop until exit.
     */
    public void run() {
        ui.showGreeting(NAME);
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (IceyException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    /**
     * Serves as the entry point of the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Icey icey = new Icey();
        // Load existing tasks from storage
        try {
            icey.tasks = icey.storage.load();
        } catch (IceyException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            icey.tasks = new TaskList();
        }
        icey.run();
    }
}
