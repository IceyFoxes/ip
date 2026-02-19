package icey.ui;

import java.util.Scanner;
import java.util.function.Consumer;

/**
 * Handles all user interface interactions including reading input and
 * displaying output.
 */
public class Ui {
    private static final String DIVIDER = "─".repeat(25);
    private static final String INDENT = "    ";
    private final Scanner scanner = new Scanner(System.in);
    private final Consumer<String> out;

    /**
     * Creates a new Ui instance that outputs to standard output.
     */
    public Ui() {
        this(System.out::println);
    }

    /**
     * Creates a new Ui instance with a custom output consumer.
     *
     * @param out The consumer that handles output lines.
     */
    public Ui(Consumer<String> out) {
        this.out = out;
    }

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
        out.accept(INDENT + DIVIDER);
        for (String message : messages) {
            out.accept(INDENT + message);
        }
        out.accept(INDENT + DIVIDER);
    }

    /**
     * Displays the greeting message.
     *
     * @param name The name of the chatbot.
     */
    public void showGreeting(String name) {
        showMessages("Yip! I'm " + name + ", your ice fox planner.",
                "What trail should we track today?");
    }

    /**
     * Displays the goodbye message.
     */
    public void showBye() {
        showMessages("Icey the fox is curling up for a nap. See you soon!");
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
