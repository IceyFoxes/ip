package icey.ui;

import java.util.Scanner;

/**
 * Handles all user interface interactions including reading input and
 * displaying output.
 */
public class Ui {
    private static final String DIVIDER = "─".repeat(60);
    private static final String INDENT = "    ";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads a command from the user.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays messages to the user with formatting.
     *
     * @param messages The messages to display.
     */
    public void showMessages(String... messages) {
        System.out.println(INDENT + DIVIDER);
        for (String message : messages) {
            System.out.println(INDENT + message);
        }
        System.out.println(INDENT + DIVIDER);
    }

    /**
     * Displays the greeting message.
     *
     * @param name The name of the chatbot.
     */
    public void showGreeting(String name) {
        showMessages("Hello! I'm " + name + ".", "What can I do for you?");
    }

    /**
     * Displays the goodbye message.
     */
    public void showBye() {
        showMessages("Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        showMessages(message.split("\n"));
    }

    /**
     * Returns the indent string for formatting.
     *
     * @return The indent string.
     */
    public String getIndent() {
        return INDENT;
    }
}
