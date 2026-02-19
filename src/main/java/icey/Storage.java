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
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IceyException("Malformed task line: " + line);
        }
        String type = parts[0];
        String doneSymbol = parts[1];
        if (!doneSymbol.equals(Task.DONE_SYMBOL) && !doneSymbol.equals(Task.NOT_DONE_SYMBOL)) {
            throw new IceyException("Invalid done status in task line: " + line);
        }
        boolean isDone = doneSymbol.equals(Task.DONE_SYMBOL);
        String description = parts[2];
        if (description.trim().isEmpty()) {
            throw new IceyException("Task description cannot be empty.");
        }

        Task task;
        int tagsIndex;
        if (type.equals(TaskType.TODO.getSymbol())) {
            task = new Todo(description);
            tagsIndex = 3;
        } else if (type.equals(TaskType.DEADLINE.getSymbol())) {
            if (parts.length < 4) {
                throw new IceyException("Malformed deadline task line: " + line);
            }
            LocalDateTime by = LocalDateTime.parse(parts[3], DATE_FORMAT);
            task = new Deadline(description, by);
            tagsIndex = 4;
        } else if (type.equals(TaskType.EVENT.getSymbol())) {
            if (parts.length < 5) {
                throw new IceyException("Malformed event task line: " + line);
            }
            LocalDateTime from = LocalDateTime.parse(parts[3], DATE_FORMAT);
            LocalDateTime to = LocalDateTime.parse(parts[4], DATE_FORMAT);
            task = new Event(description, from, to);
            tagsIndex = 5;
        } else {
            throw new IceyException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        if (parts.length > tagsIndex) {
            for (String tag : parts[tagsIndex].split(" ")) {
                if (!tag.isBlank()) {
                    task.addTag(tag);
                }
            }
        }
        return task;
    }

    private String formatTask(Task task) {
        String base = task.toStorageString(DELIMITER, DATE_FORMAT);
        if (task.getTags().isEmpty()) {
            return base;
        }
        return base + DELIMITER + String.join(" ", task.getTags());
    }
}
