import java.util.Scanner;
import java.util.ArrayList;

public class Icey {
    private static final String NAME = "Icey";
    private static final String DIVIDER = "─".repeat(60);
    private static final String INDENT = "    ";
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<Task> tasks = new ArrayList<>();

    // For indentation and future enhancements
    private static void reply(String... messages) {
        System.out.println(INDENT + DIVIDER);
        for (String message : messages) {
            System.out.println(INDENT + message);
        }
        System.out.println(INDENT + DIVIDER);
    }

    private static void greetUser() {
        Icey.reply("Hello! I'm " + NAME + ".",
                "What can I do for you?");
    }

    private static boolean handleExit(String[] parts) {
        if (parts.length > 1) {
            Icey.reply("Bye command does not take any arguments.",
                    "Usage: bye");
            return false;
        }
        Icey.reply("Bye. Hope to see you again soon!");
        return true;
    }

    private static void addTask(Task task) {
        tasks.add(task);
        Icey.reply("I've added Task:", INDENT + task.toString(),
                tasks.size() + " tasks (" + countPendingTasks() + " pending) in the list.");
    }

    private static int countPendingTasks() {
        int pendingTasks = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                pendingTasks++;
            }
        }
        return pendingTasks;
    }

    private static void handleList(String[] parts) {
        if (parts.length > 1) {
            Icey.reply("List command does not take any arguments.",
                    "Usage: list");
            return;
        }
        if (tasks.isEmpty()) {
            Icey.reply("No tasks yet!");
            return;
        }

        String[] taskLines = new String[tasks.size() + 1];
        taskLines[0] = "Here are the tasks:";

        for (int i = 0; i < tasks.size(); i++) {
            taskLines[i + 1] = INDENT + (i + 1) + ". " + tasks.get(i);
        }

        Icey.reply(taskLines);
    }

    private static void markTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            Icey.reply("Invalid task number.");
            return;
        }
        Task task = tasks.get(index);
        if (task.isDone()) {
            Icey.reply("Task is already marked as done.");
            return;
        }
        task.isDone = true;
        Icey.reply("Task marked as done:", INDENT + task.toString());
    }

    private static void unmarkTask(int index) {
        if (index < 0 || index >= tasks.size()) {
            Icey.reply("Invalid task number.");
            return;
        }

        Task task = tasks.get(index);
        if (!task.isDone()) {
            Icey.reply("Task is already marked as not done.");
            return;
        }
        task.isDone = false;
        Icey.reply("Task marked as not done:", INDENT + task.toString());
    }

    private static Integer parseTaskIndex(String indexStr) {
        try {
            return Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void handleMark(String[] parts) {
        if (parts.length < 2) {
            Icey.reply("Please specify the task number to mark as done.",
                    "Usage: mark <task number>");
            return;
        }
        Integer index = parseTaskIndex(parts[1]);
        if (index == null) {
            Icey.reply("Invalid task number format.");
            return;
        }
        Icey.markTask(index);
    }

    private static void handleUnmark(String[] parts) {
        if (parts.length < 2) {
            Icey.reply("Please specify the task number to mark as not done.",
                    "Usage: unmark <task number>");
            return;
        }
        Integer index = parseTaskIndex(parts[1]);
        if (index == null) {
            Icey.reply("Invalid task number format.");
            return;
        }
        Icey.unmarkTask(index);
    }

    private static void handleTodo(String[] parts) {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            Icey.reply("The description of a todo cannot be empty.",
                    "Usage: todo <description>");
            return;
        }
        Task todoTask = new Todo(parts[1].trim());
        Icey.addTask(todoTask);
    }

    private static void handleDeadline(String[] parts) {
        if (parts.length < 2 || !parts[1].contains(" /by ")) {
            Icey.reply("Usage: deadline <description> /by <date>");
            return;
        }
        String[] deadlineParts = parts[1].split(" /by ", 2);
        if (deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            Icey.reply("The description and deadline cannot be empty.",
                    "Usage: deadline <description> /by <date>");
            return;
        }
        Task deadlineTask = new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim());
        Icey.addTask(deadlineTask);
    }

    private static void handleEvent(String[] parts) {
        if (parts.length < 2 || !parts[1].contains(" /from ") || !parts[1].contains(" /to ")) {
            Icey.reply("Usage: event <description> /from <start time> /to <end time>");
            return;
        }
        String[] eventParts1 = parts[1].split(" /from ", 2);
        String[] eventParts2 = eventParts1[1].split(" /to ", 2);
        if (eventParts1[0].trim().isEmpty() || eventParts2[0].trim().isEmpty() || eventParts2[1].trim().isEmpty()) {
            Icey.reply("The description, start time, and end time cannot be empty.",
                    "Usage: event <description> /from <start time> /to <end time>");
            return;
        }
        Task eventTask = new Event(eventParts1[0].trim(), eventParts2[0].trim(), eventParts2[1].trim());
        Icey.addTask(eventTask);
    }

    public static void main(String[] args) {
        Icey.greetUser();
        // Main interaction loop
        while (true) {
            String input = Icey.scanner.nextLine();
            String[] parts = input.split(" ", 2);
            // First find the command for the switch case, then each command parses its own
            // arguments
            String command = parts[0];

            switch (command) {
                case "bye":
                    if (Icey.handleExit(parts)) {
                        return;
                    }
                    continue;
                case "list":
                    Icey.handleList(parts);
                    continue;
                case "mark":
                    Icey.handleMark(parts);
                    continue;
                case "unmark":
                    Icey.handleUnmark(parts);
                    continue;
                case "todo":
                    Icey.handleTodo(parts);
                    continue;
                case "deadline":
                    Icey.handleDeadline(parts);
                    continue;
                case "event":
                    Icey.handleEvent(parts);
                    continue;
                default:
                    Task task = new Task(input);
                    Icey.addTask(task);
            }
        }
    }
}
