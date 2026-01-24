import java.util.Scanner;
import java.util.ArrayList;

/**
 * Represents the main chatbot application that manages tasks. Supports
 * creating, listing, marking, and deleting tasks through a command-line
 * interface.
 */
public class Icey {
    private static final String NAME = "Icey";
    private static final String DIVIDER = "─".repeat(60);
    private static final String INDENT = "    ";
    private final Scanner scanner = new Scanner(System.in);
    private ArrayList<Task> tasks = new ArrayList<>();

    // For indentation and future enhancements
    private void reply(String... messages) {
        System.out.println(INDENT + DIVIDER);
        for (String message : messages) {
            System.out.println(INDENT + message);
        }
        System.out.println(INDENT + DIVIDER);
    }

    private void greetUser() {
        reply("Hello! I'm " + NAME + ".", "What can I do for you?");
    }

    private boolean handleExit(String[] parts) throws IceyException {
        if (parts.length > 1) {
            throw new IceyException("Bye command does not take any arguments.\nUsage: bye");
        }
        reply("Bye. Hope to see you again soon!");
        return true;
    }

    private void addTask(Task task) {
        tasks.add(task);
        reply("I've added Task:", INDENT + task.toString(),
                tasks.size() + " tasks (" + countPendingTasks() + " pending) in the list.");
    }

    private int countPendingTasks() {
        int pendingTasks = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                pendingTasks++;
            }
        }
        return pendingTasks;
    }

    private void handleList(String[] parts) throws IceyException {
        if (parts.length > 1) {
            throw new IceyException("List command does not take any arguments.\nUsage: list");
        }
        if (tasks.isEmpty()) {
            reply("No tasks yet!");
            return;
        }

        String[] taskLines = new String[tasks.size() + 1];
        taskLines[0] = "Here are the tasks:";

        for (int i = 0; i < tasks.size(); i++) {
            taskLines[i + 1] = INDENT + (i + 1) + ". " + tasks.get(i);
        }

        reply(taskLines);
    }

    private void markTask(int index) throws IceyException {
        Task task = tasks.get(index);
        if (task.isDone()) {
            throw new IceyException("Task is already marked as done.");
        }
        task.isDone = true;
        reply("Task marked as done:", INDENT + task.toString());
    }

    private void unmarkTask(int index) throws IceyException {
        Task task = tasks.get(index);
        if (!task.isDone()) {
            throw new IceyException("Task is already marked as not done.");
        }
        task.isDone = false;
        reply("Task marked as not done:", INDENT + task.toString());
    }

    private int parseTaskIndex(String indexStr) throws IceyException {
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index < 0 || index >= tasks.size()) {
                throw new IceyException("Invalid task number.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new IceyException("Invalid task number format.");
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
            throw new IceyException("Usage: deadline <description> /by <date>");
        }
        String[] deadlineParts = parts[1].split(" /by ", 2);
        if (deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new IceyException(
                    "The description and deadline cannot be empty.\nUsage: deadline <description> /by <date>");
        }
        Task deadlineTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        addTask(deadlineTask);
    }

    private void handleEvent(String[] parts) throws IceyException {
        if (parts.length < 2 || !parts[1].contains(" /from ") || !parts[1].contains(" /to ")) {
            throw new IceyException("Usage: event <description> /from <start time> /to <end time>");
        }
        String[] eventParts1 = parts[1].split(" /from ", 2);
        String[] eventParts2 = eventParts1[1].split(" /to ", 2);
        if (eventParts1[0].trim().isEmpty() || eventParts2[0].trim().isEmpty()
                || eventParts2[1].trim().isEmpty()) {
            throw new IceyException(
                    "The description, start time, and end time cannot be empty.\nUsage: event <description> /from <start time> /to <end time>");
        }
        Task eventTask = new Event(eventParts1[0].trim(), eventParts2[0].trim(), eventParts2[1].trim());
        addTask(eventTask);
    }

    private void handleDelete(String[] parts) throws IceyException {
        if (parts.length < 2) {
            throw new IceyException("Please specify the task number to delete.\nUsage: delete <task number>");
        }
        int index = parseTaskIndex(parts[1]);
        Task task = tasks.remove(index);
        reply("I've removed this task:", INDENT + task.toString(),
                tasks.size() + " tasks (" + countPendingTasks() + " pending) in the list.");
    }

    /**
     * Starts the chatbot and processes user commands in a loop until exit.
     */
    public void run() {
        greetUser();
        // Main interaction loop
        while (true) {
            try {
                String input = scanner.nextLine();
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
                    reply("Command not recognized.",
                            "Available commands: todo, deadline, event, list, mark, unmark, bye");
                    continue;
                }
            } catch (IceyException e) {
                reply(e.getMessage().split("\n"));
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
        icey.run();
    }
}
