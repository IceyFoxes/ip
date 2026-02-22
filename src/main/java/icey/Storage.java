package icey;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import icey.task.Deadline;
import icey.task.Event;
import icey.task.Task;
import icey.task.TaskList;
import icey.task.TaskType;
import icey.task.Todo;

/**
 * Handles loading and saving tasks to a file.
 */
public class Storage {
    private static final String DELIMITER = " | ";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private final Path filePath;

    /**
     * Creates a new Storage instance for the specified file path.
     *
     * @param filePath The path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Loads tasks from the storage file. Creates the file and parent directories if
     * they don't exist.
     *
     * @return A TaskList containing the loaded tasks.
     * @throws IceyException If there is an error reading the file.
     */
    public TaskList load() throws IceyException {
        TaskList tasks = new TaskList();
        int corruptedLineCount = 0;

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (!line.trim().isEmpty()) {
                    try {
                        Task task = parseTask(line);
                        tasks.add(task);
                    } catch (IceyException | RuntimeException e) {
                        corruptedLineCount++;
                    }
                }
            }
        } catch (IOException e) {
            throw new IceyException("Error loading tasks: " + e.getMessage());
        }

        if (corruptedLineCount > 0) {
            System.err.println("Warning: " + corruptedLineCount
                    + " corrupted task line(s) were skipped while loading data.");
        }

        return tasks;
    }

    /**
     * Saves the tasks to the storage file.
     *
     * @param tasks The TaskList to save.
     * @throws IceyException If there is an error writing to the file.
     */
    public void save(TaskList tasks) throws IceyException {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks.getAll()) {
            lines.add(formatTask(task));
        }

        try {
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new IceyException("Error saving tasks: " + e.getMessage());
        }
    }

    private Task parseTask(String line) throws IceyException {
        if (line == null || line.trim().isEmpty()) {
            throw new IceyException("Task line cannot be empty.");
        }
        int cursor = 0;
        String type = readUntilDelimiter(line, cursor);
        cursor += type.length() + DELIMITER.length();

        String doneSymbol = readUntilDelimiter(line, cursor);
        cursor += doneSymbol.length() + DELIMITER.length();

        if (!doneSymbol.equals(Task.DONE_SYMBOL) && !doneSymbol.equals(Task.NOT_DONE_SYMBOL)) {
            throw new IceyException("Invalid done status in task line: " + line);
        }
        boolean isDone = doneSymbol.equals(Task.DONE_SYMBOL);

        LengthPrefixedField descriptionField = readLengthPrefixedField(line, cursor);
        String description = descriptionField.value;
        cursor = descriptionField.nextIndex;

        if (description.trim().isEmpty()) {
            throw new IceyException("Task description cannot be empty.");
        }

        Task task;
        if (type.equals(TaskType.TODO.getSymbol())) {
            task = new Todo(description);
        } else if (type.equals(TaskType.DEADLINE.getSymbol())) {
            if (!hasDelimiterAt(line, cursor)) {
                throw new IceyException("Malformed deadline task line: " + line);
            }
            cursor += DELIMITER.length();
            String byText = readUntilDelimiterOrEnd(line, cursor);
            LocalDateTime by = LocalDateTime.parse(byText, DATE_FORMAT);
            cursor += byText.length();
            task = new Deadline(description, by);
        } else if (type.equals(TaskType.EVENT.getSymbol())) {
            if (!hasDelimiterAt(line, cursor)) {
                throw new IceyException("Malformed event task line: " + line);
            }
            cursor += DELIMITER.length();
            String fromText = readUntilDelimiter(line, cursor);
            cursor += fromText.length();

            if (!hasDelimiterAt(line, cursor)) {
                throw new IceyException("Malformed event task line: " + line);
            }
            cursor += DELIMITER.length();
            String toText = readUntilDelimiterOrEnd(line, cursor);
            cursor += toText.length();

            LocalDateTime from = LocalDateTime.parse(fromText, DATE_FORMAT);
            LocalDateTime to = LocalDateTime.parse(toText, DATE_FORMAT);
            task = new Event(description, from, to);
        } else {
            throw new IceyException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        if (hasDelimiterAt(line, cursor)) {
            cursor += DELIMITER.length();
            LengthPrefixedField tagsField = readLengthPrefixedField(line, cursor);
            if (tagsField.nextIndex != line.length()) {
                throw new IceyException("Malformed trailing data in task line: " + line);
            }
            for (String tag : parseTagsPayload(tagsField.value)) {
                task.addTag(tag);
            }
        } else if (cursor != line.length()) {
            throw new IceyException("Malformed trailing data in task line: " + line);
        }
        return task;
    }

    private String formatTask(Task task) {
        String base = task.getType().getSymbol()
                + DELIMITER + (task.isDone() ? Task.DONE_SYMBOL : Task.NOT_DONE_SYMBOL)
                + DELIMITER + toLengthPrefixed(task.getDescription());

        if (task instanceof Deadline) {
            base += DELIMITER + ((Deadline) task).getBy().format(DATE_FORMAT);
        } else if (task instanceof Event) {
            base += DELIMITER + ((Event) task).getFrom().format(DATE_FORMAT)
                    + DELIMITER + ((Event) task).getTo().format(DATE_FORMAT);
        }

        if (!task.getTags().isEmpty()) {
            base += DELIMITER + toLengthPrefixed(toTagsPayload(task.getTags()));
        }
        return base;
    }

    private static String toTagsPayload(List<String> tags) {
        StringBuilder payload = new StringBuilder();
        for (String tag : tags) {
            payload.append(toLengthPrefixed(tag));
        }
        return payload.toString();
    }

    private static List<String> parseTagsPayload(String payload) throws IceyException {
        ArrayList<String> tags = new ArrayList<>();
        int cursor = 0;
        while (cursor < payload.length()) {
            LengthPrefixedField tagField = readLengthPrefixedField(payload, cursor);
            tags.add(tagField.value);
            cursor = tagField.nextIndex;
        }
        return tags;
    }

    private static boolean hasDelimiterAt(String line, int index) {
        return index >= 0 && index + DELIMITER.length() <= line.length()
                && line.startsWith(DELIMITER, index);
    }

    private static String readUntilDelimiter(String line, int start) throws IceyException {
        int delimiterIndex = line.indexOf(DELIMITER, start);
        if (delimiterIndex < 0) {
            throw new IceyException("Malformed task line: " + line);
        }
        return line.substring(start, delimiterIndex);
    }

    private static String readUntilDelimiterOrEnd(String line, int start) {
        int delimiterIndex = line.indexOf(DELIMITER, start);
        if (delimiterIndex < 0) {
            return line.substring(start);
        }
        return line.substring(start, delimiterIndex);
    }

    private static String toLengthPrefixed(String value) {
        return value.length() + ":" + value;
    }

    private static LengthPrefixedField readLengthPrefixedField(String line, int start) throws IceyException {
        int colonIndex = line.indexOf(':', start);
        if (colonIndex < 0) {
            throw new IceyException("Malformed length-prefixed field: " + line);
        }

        String lengthText = line.substring(start, colonIndex);
        int length;
        try {
            length = Integer.parseInt(lengthText);
        } catch (NumberFormatException e) {
            throw new IceyException("Invalid field length in task line: " + line);
        }

        if (length < 0) {
            throw new IceyException("Invalid negative field length in task line: " + line);
        }

        int valueStart = colonIndex + 1;
        int valueEnd = valueStart + length;
        if (valueEnd > line.length()) {
            throw new IceyException("Field length exceeds line size: " + line);
        }

        return new LengthPrefixedField(line.substring(valueStart, valueEnd), valueEnd);
    }

    private static class LengthPrefixedField {
        private final String value;
        private final int nextIndex;

        private LengthPrefixedField(String value, int nextIndex) {
            this.value = value;
            this.nextIndex = nextIndex;
        }
    }
}
