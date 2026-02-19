package icey;

import java.util.function.Consumer;

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
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;
    private boolean isExit;

    /**
     * Constructs an Icey chatbot instance, initialising the UI, storage, and task list.
     */
    public Icey() {
        this.ui = new Ui();
        this.storage = new Storage(DATA_PATH);
        this.tasks = loadTasks();
    }

    private TaskList loadTasks() {
        try {
            return storage.load();
        } catch (IceyException e) {
            ui.showError(e.getMessage());
            return new TaskList();
        }
    }

    /**
     * Starts the chatbot and processes user commands in a loop until exit.
     */
    public void run() {
        ui.showGreeting(NAME);
        isExit = false;
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
     * Returns the greeting message from the chatbot.
     *
     * @return The greeting message string.
     */
    public String getGreeting() {
        return captureOutput(responseUi -> responseUi.showGreeting(NAME));
    }

    /**
     * Processes the user's input and returns the chatbot's response.
     *
     * @param input The user's input string.
     * @return The chatbot's response string.
     */
    public String getResponse(String input) {
        isExit = false;
        return captureOutput(responseUi -> {
            try {
                Command c = Parser.parse(input);
                c.execute(tasks, responseUi, storage);
                isExit = c.isExit();
            } catch (IceyException e) {
                responseUi.showError(e.getMessage());
            }
        });
    }

    public boolean isExit() {
        return isExit;
    }

    private String captureOutput(Consumer<Ui> action) {
        StringBuilder sb = new StringBuilder();
        Ui responseUi = new Ui(line -> sb.append(line).append("\n"));
        action.accept(responseUi);
        return sb.toString().stripTrailing();
    }

    /**
     * Serves as the entry point of the application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Icey().run();
    }
}
