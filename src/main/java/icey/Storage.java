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

        try {
            if (!Files.exists(filePath)) {
                Files.createDirectories(filePath.getParent());
                Files.createFile(filePath);
                return tasks;
            }

            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Task task = parseTask(line);
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            throw new IceyException("Error loading tasks: " + e.getMessage());
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
        String[] parts = line.split(" \\| ");
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        if (type.equals(TaskType.TODO.getSymbol())) {
            task = new Todo(description);
        } else if (type.equals(TaskType.DEADLINE.getSymbol())) {
            LocalDateTime by = LocalDateTime.parse(parts[3], DATE_FORMAT);
            task = new Deadline(description, by);
        } else if (type.equals(TaskType.EVENT.getSymbol())) {
            LocalDateTime from = LocalDateTime.parse(parts[3], DATE_FORMAT);
            LocalDateTime to = LocalDateTime.parse(parts[4], DATE_FORMAT);
            task = new Event(description, from, to);
        } else {
            throw new IceyException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    private String formatTask(Task task) {
        String type = task.getType().getSymbol();
        String done = task.isDone() ? "1" : "0";
        String desc = task.getDescription();

        if (task instanceof Todo) {
            return type + DELIMITER + done + DELIMITER + desc;
        } else if (task instanceof Deadline) {
            Deadline d = (Deadline) task;
            String by = d.getBy().format(DATE_FORMAT);
            return type + DELIMITER + done + DELIMITER + desc + DELIMITER + by;
        } else {
            Event e = (Event) task;
            String from = e.getFrom().format(DATE_FORMAT);
            String to = e.getTo().format(DATE_FORMAT);
            return type + DELIMITER + done + DELIMITER + desc + DELIMITER + from + DELIMITER + to;
        }
    }
}
