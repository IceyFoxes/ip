import java.util.Scanner;

public class Icey {
    private static final String NAME = "Icey";
    private static final String DIVIDER = "─".repeat(60);
    private static final Scanner scanner = new Scanner(System.in);

    // For indentation and future enhancements
    private static void reply(String... messages) {
        System.out.println("    " + DIVIDER);
        for (String message : messages) {
            System.out.println("    " + message);
        }
        System.out.println("    " + DIVIDER);
    }

    private static void greetUser() {
        Icey.reply("Hello! I'm " + NAME + ".",
                "What can I do for you?");
    }

    private static void exit() {
        Icey.reply("Bye. Hope to see you again soon!");
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

            // Echo the user input
            Icey.reply(input);
        }
    }
}
