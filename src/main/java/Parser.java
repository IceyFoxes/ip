import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles parsing of user input and commands.
 */
public class Parser {
    private static final DateTimeFormatter INPUT_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses a date/time string into a LocalDateTime.
     *
     * @param dateStr The date string in yyyy-MM-dd HHmm format.
     * @return The parsed LocalDateTime.
     * @throws IceyException If the date format is invalid.
     */
    public LocalDateTime parseDateTime(String dateStr) throws IceyException {
        try {
            return LocalDateTime.parse(dateStr, INPUT_DATE_FORMAT);
        } catch (DateTimeParseException e) {
            throw new IceyException("Invalid date format. Please use: yyyy-MM-dd HHmm");
        }
    }

    /**
     * Parses and validates a task index from user input.
     *
     * @param indexStr The index string from user input.
     * @param listSize The current size of the task list.
     * @return The zero-based index.
     * @throws IceyException If the index is invalid.
     */
    public int parseTaskIndex(String indexStr, int listSize) throws IceyException {
        try {
            int index = Integer.parseInt(indexStr) - 1;
            if (index < 0 || index >= listSize) {
                throw new IceyException("Invalid task number.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new IceyException("Invalid task number format.");
        }
    }

    /**
     * Parses a todo command and creates a Todo task.
     *
     * @param parts The command parts.
     * @return The created Todo task.
     * @throws IceyException If the command format is invalid.
     */
    public Task parseTodo(String[] parts) throws IceyException {
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new IceyException("The description of a todo cannot be empty.\nUsage: todo <description>");
        }
        return new Todo(parts[1].trim());
    }

    /**
     * Parses a deadline command and creates a Deadline task.
     *
     * @param parts The command parts.
     * @return The created Deadline task.
     * @throws IceyException If the command format is invalid.
     */
    public Task parseDeadline(String[] parts) throws IceyException {
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

    /**
     * Parses an event command and creates an Event task.
     *
     * @param parts The command parts.
     * @return The created Event task.
     * @throws IceyException If the command format is invalid.
     */
    public Task parseEvent(String[] parts) throws IceyException {
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
        return new Event(eventParts1[0].trim(), from, to);
    }
}
