import java.util.Scanner;
import java.util.ArrayList;

public class Icey {
    private static final String NAME = "Icey";
    private static final String DIVIDER = "─".repeat(60);
    private static final String INDENT = "    ";
    private static final Scanner scanner = new Scanner(System.in);
    private static ArrayList<String> tasks = new ArrayList<>();

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

    private static void addTask(String task) {
        tasks.add(task);
        Icey.reply("I've added Task: " + task + ".",
                tasks.size() + " tasks pending.");
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

    public static void main(String[] args) {
        Icey.greetUser();
        // Main interaction loop
        while (true) {
            String input = Icey.scanner.nextLine();
            if (input.equals("bye")) {
                Icey.exit();
                break;
            }

            if (input.equals("list")) {
                Icey.listTasks();
                continue;
            }

            // Store the user input
            Icey.addTask(input);
        }
    }
}
