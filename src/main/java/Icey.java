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

    private static void exit() {
        Icey.reply("Bye. Hope to see you again soon!");
    }

    private static void addTask(String input) {
        Task task = new Task(input);
        tasks.add(task);
        Icey.reply("I've added Task: " + input + ".");
    }

    private static void listTasks() {
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
                    Icey.exit();
                    break;
                case "list":
                    Icey.listTasks();
                    continue;
                case "mark":
                    if (parts.length < 2) {
                        Icey.reply("Please specify the task number to mark as done.",
                                "Usage: mark <task number>");
                        continue;
                    }
                    try {
                        Integer index = Integer.parseInt(parts[1]) - 1;
                        Icey.markTask(index);
                    } catch (NumberFormatException e) {
                        Icey.reply("Invalid task number format.");
                        continue;
                    }
                    continue;
                case "unmark":
                    if (parts.length < 2) {
                        Icey.reply("Please specify the task number to mark as not done.",
                                "Usage: unmark <task number>");
                        continue;
                    }
                    try {
                        Integer index = Integer.parseInt(parts[1]) - 1;
                        Icey.unmarkTask(index);
                    } catch (NumberFormatException e) {
                        Icey.reply("Invalid task number format.");
                        continue;
                    }
                    continue;
                case "todo":
                    // TODO
                    continue;
                case "deadline":
                    // TODO
                    continue;
                case "event":
                    // TODO
                    continue;
                default:
                    Icey.addTask(input);
            }
        }
    }
}
