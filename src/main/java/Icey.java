import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents the main chatbot application that manages tasks. Supports
 * creating, listing, marking, and deleting tasks through a command-line
 * interface.
 */
public class Icey {
    private static final String NAME = "Icey";
    private static final String DATA_PATH = "data/icey.txt";
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private final Ui ui = new Ui();
    private final Storage storage = new Storage(DATA_PATH);
    private TaskList tasks;

    private boolean handleExit(String[] parts) throws IceyException {
        if (parts.length > 1) {
            throw new IceyException("Bye command does not take any arguments.\nUsage: bye");
        }
        ui.showBye();
        return true;
    }

    private void addTask(Task task) throws IceyException {
        tasks.add(task);
        storage.save(tasks);
        ui.showMessages("I've added Task:", ui.getIndent() + task.toString(),
                tasks.getSize() + " tasks (" + countPendingTasks() + " pending) in the list.");
    }

    private int countPendingTasks() {
        return tasks.countPending();
    }

    private void handleList(String[] parts) throws IceyException {
        if (parts.length > 1) {
            throw new IceyException("List command does not take any arguments.\nUsage: list");
        }
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

    private void markTask(int index) throws IceyException {
        Task task = tasks.get(index);
        if (task.isDone()) {
            throw new IceyException("Task is already marked as done.");
        }
        task.isDone = true;
        storage.save(tasks);
        ui.showMessages("Task marked as done:", ui.getIndent() + task.toString());
    }

    private void unmarkTask(int index) throws IceyException {
        Task task = tasks.get(index);
        if (!task.isDone()) {
            throw new IceyException("Task is already marked as not done.");
        }
        task.isDone = false;
        storage.save(tasks);
        ui.showMessages("Task marked as not done:", ui.getIndent() + task.toString());
    }

    private int parseTaskIndex(String indexStr) throws IceyException {
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index < 0 || index >= tasks.getSize()) {
                throw new IceyException("Invalid task number.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new IceyException("Invalid task number format.");
        }
    }

    private LocalDateTime parseDateTime(String dateStr) throws IceyException {
        try {
            return LocalDateTime.parse(dateStr, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IceyException("Invalid date format. Please use: yyyy-MM-dd HHmm");
        }
    }

    private void handleMark(String[] parts) throws IceyException {
        if (parts.length < 2) {
            throw new IceyException(
                    "Please specify the task number to mark as done.\nUsage: mark <task number>");
        }
        int index = parseTaskIndex(parts[1]);
        markTask(index);
    }

    private void handleUnmark(String[] parts) throws IceyException {
        if (parts.length < 2) {
            throw new IceyException(
                    "Please specify the task number to mark as not done.\nUsage: unmark <task number>");
        }
        int index = parseTaskIndex(parts[1]);
        unmarkTask(index);
    }

    private void handleTodo(String[] parts) throws IceyException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new IceyException("The description of a todo cannot be empty.\nUsage: todo <description>");
        }
        Task todoTask = new Todo(parts[1].trim());
        addTask(todoTask);
    }

    private void handleDeadline(String[] parts) throws IceyException {
        if (parts.length < 2 || !parts[1].contains(" /by ")) {
            throw new IceyException("Usage: deadline <description> /by <yyyy-MM-dd HHmm>");
        }
        String[] deadlineParts = parts[1].split(" /by ", 2);
        if (deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new IceyException(
                    "The description and deadline cannot be empty.\nUsage: deadline <description> /by <yyyy-MM-dd HHmm>");
        }
        LocalDateTime by = parseDateTime(deadlineParts[1].trim());
        Task deadlineTask = new Deadline(deadlineParts[0].trim(), by);
        addTask(deadlineTask);
    }

    private void handleEvent(String[] parts) throws IceyException {
        if (parts.length < 2 || !parts[1].contains(" /from ") || !parts[1].contains(" /to ")) {
            throw new IceyException("Usage: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        String[] eventParts1 = parts[1].split(" /from ", 2);
        String[] eventParts2 = eventParts1[1].split(" /to ", 2);
        if (eventParts1[0].trim().isEmpty() || eventParts2[0].trim().isEmpty()
                || eventParts2[1].trim().isEmpty()) {
            throw new IceyException(
                    "The description, start time, and end time cannot be empty.\nUsage: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
        }
        LocalDateTime from = parseDateTime(eventParts2[0].trim());
        LocalDateTime to = parseDateTime(eventParts2[1].trim());
        Task eventTask = new Event(eventParts1[0].trim(), from, to);
        addTask(eventTask);
    }

    private void handleDelete(String[] parts) throws IceyException {
        if (parts.length < 2) {
            throw new IceyException("Please specify the task number to delete.\nUsage: delete <task number>");
        }
        int index = parseTaskIndex(parts[1]);
        Task task = tasks.remove(index);
        storage.save(tasks);
        ui.showMessages("I've removed this task:", ui.getIndent() + task.toString(),
                tasks.getSize() + " tasks (" + countPendingTasks() + " pending) in the list.");
    }

    /**
     * Starts the chatbot and processes user commands in a loop until exit.
     */
    public void run() {
        ui.showGreeting(NAME);
        // Main interaction loop
        while (true) {
            try {
                String input = ui.readCommand();
                String[] parts = input.split(" ", 2);
                // First find the command for the switch case,
                // then each command parses its own arguments
                String command = parts[0];

                switch (command) {
                case "bye":
                    if (handleExit(parts)) {
                        return;
                    }
                    continue;
                case "list":
                    handleList(parts);
                    continue;
                case "mark":
                    handleMark(parts);
                    continue;
                case "unmark":
                    handleUnmark(parts);
                    continue;
                case "todo":
                    handleTodo(parts);
                    continue;
                case "deadline":
                    handleDeadline(parts);
                    continue;
                case "event":
                    handleEvent(parts);
                    continue;
                case "delete":
                    handleDelete(parts);
                    continue;
                default:
                    ui.showMessages("Command not recognized.",
                            "Available commands: todo, deadline, event, list, mark, unmark, bye");
                    continue;
                }
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
        try {
            icey.tasks = icey.storage.load();
        } catch (IceyException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            icey.tasks = new TaskList();
        }
        icey.run();
    }
}
