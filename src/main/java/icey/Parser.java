package icey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import icey.command.AddCommand;
import icey.command.ByeCommand;
import icey.command.Command;
import icey.command.DeleteCommand;
import icey.command.ListCommand;
import icey.command.MarkCommand;
import icey.command.UnmarkCommand;
import icey.task.Deadline;
import icey.task.Event;
import icey.task.Task;
import icey.task.Todo;

/**
 * Handles parsing of user input and commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses user input and returns the appropriate Command.
     *
     * @param fullCommand The full command string from the user.
     * @return The Command to execute.
     * @throws IceyException If the command is invalid.
     */
    public static Command parse(String fullCommand) throws IceyException {
        String[] parts = fullCommand.split(" ", 2);
        String commandWord = parts[0];

        switch (commandWord) {
        case "bye":
            if (parts.length > 1) {
                throw new IceyException("Bye command does not take any arguments.\nUsage: bye");
            }
            return new ByeCommand();
        case "list":
            if (parts.length > 1) {
                throw new IceyException("List command does not take any arguments.\nUsage: list");
            }
            return new ListCommand();
        case "mark":
            if (parts.length < 2) {
                throw new IceyException(
                        "Please specify the task number to mark as done.\nUsage: mark <task number>");
            }
            return new MarkCommand(parseIndex(parts[1]));
        case "unmark":
            if (parts.length < 2) {
                throw new IceyException(
                        "Please specify the task number to mark as not done.\nUsage: unmark <task number>");
            }
            return new UnmarkCommand(parseIndex(parts[1]));
        case "delete":
            if (parts.length < 2) {
                throw new IceyException(
                        "Please specify the task number to delete.\nUsage: delete <task number>");
            }
            return new DeleteCommand(parseIndex(parts[1]));
        case "todo":
            return new AddCommand(parseTodo(parts));
        case "deadline":
            return new AddCommand(parseDeadline(parts));
        case "event":
            return new AddCommand(parseEvent(parts));
        default:
            throw new IceyException("Command not recognized.\n"
                    + "Available commands: todo, deadline, event, list, mark, unmark, delete, bye");
        }
    }

    private static int parseIndex(String indexStr) throws IceyException {
        try {
            return Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            throw new IceyException("Invalid task number format.");
        }
    }

    private static LocalDateTime parseDateTime(String dateStr) throws IceyException {
        try {
            return LocalDateTime.parse(dateStr, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IceyException("Invalid date format. Please use: yyyy-MM-dd HHmm");
        }
    }

    private static Task parseTodo(String[] parts) throws IceyException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new IceyException("The description of a todo cannot be empty.\nUsage: todo <description>");
        }
        return new Todo(parts[1].trim());
    }

    private static Task parseDeadline(String[] parts) throws IceyException {
        if (parts.length < 2 || !parts[1].contains(" /by ")) {
            throw new IceyException("Usage: deadline <description> /by <yyyy-MM-dd HHmm>");
        }
        String[] deadlineParts = parts[1].split(" /by ", 2);
        if (deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
            throw new IceyException(
                    "The description and deadline cannot be empty.\nUsage: deadline <description> /by <yyyy-MM-dd HHmm>");
        }
        LocalDateTime by = parseDateTime(deadlineParts[1].trim());
        return new Deadline(deadlineParts[0].trim(), by);
    }

    private static Task parseEvent(String[] parts) throws IceyException {
        if (parts.length < 2 || !parts[1].contains(" /from ") || !parts[1].contains(" /to ")) {
            throw new IceyException(
                    "Usage: event <description> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>");
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
        return new Event(eventParts1[0].trim(), from, to);
    }
}
