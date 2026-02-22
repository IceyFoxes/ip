package icey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import icey.task.Deadline;
import icey.task.TaskList;
import icey.task.Todo;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void load_corruptedLinesPresent_loadsValidLinesOnly() throws Exception {
        Path dataFile = tempDir.resolve("icey.txt");
        Files.write(dataFile, List.of(
            "T | 0 | 11:borrow book",
            "BAD LINE",
            "D | 1 | 11:return book | 2019-12-02T18:00:00",
            "E | 0 | 7:meeting | invalid-from | 2019-12-02T16:00:00"
        ));

        Storage storage = new Storage(dataFile.toString());
        TaskList tasks = storage.load();

        assertEquals(2, tasks.getSize());
        assertEquals("borrow book", tasks.get(0).getDescription());
        assertEquals("return book", tasks.get(1).getDescription());
        assertTrue(tasks.get(1).isDone());
    }

    @Test
    public void load_missingFile_createsFileAndReturnsEmptyList() throws Exception {
        Path dataFile = tempDir.resolve("new-icey.txt");
        Storage storage = new Storage(dataFile.toString());

        TaskList tasks = storage.load();

        assertEquals(0, tasks.getSize());
        assertTrue(Files.exists(dataFile));
    }

    @Test
    public void saveAndLoad_descriptionContainsDelimiter_roundTripsCorrectly() throws Exception {
        Path dataFile = tempDir.resolve("icey.txt");
        Storage storage = new Storage(dataFile.toString());
        TaskList source = new TaskList();
        Todo todo = new Todo("borrow | return book");
        todo.addTag("#x");
        Deadline deadline = new Deadline("meet | discuss", LocalDateTime.of(2026, 2, 22, 18, 0));
        source.add(todo);
        source.add(deadline);

        storage.save(source);
        TaskList loaded = storage.load();

        assertEquals(2, loaded.getSize());
        assertEquals("borrow | return book", loaded.get(0).getDescription());
        assertEquals("meet | discuss", loaded.get(1).getDescription());
        assertEquals(1, loaded.get(0).getTags().size());
        assertEquals("#x", loaded.get(0).getTags().get(0));
    }

    @Test
    public void saveAndLoad_tagsWithSpacesAndDelimiter_roundTripsCorrectly() throws Exception {
        Path dataFile = tempDir.resolve("icey-tags.txt");
        Storage storage = new Storage(dataFile.toString());
        TaskList source = new TaskList();
        Todo todo = new Todo("tag payload test");
        todo.addTag("#very important");
        todo.addTag("#ops | prod");
        source.add(todo);

        storage.save(source);
        TaskList loaded = storage.load();

        assertEquals(1, loaded.getSize());
        assertEquals(2, loaded.get(0).getTags().size());
        assertEquals("#very important", loaded.get(0).getTags().get(0));
        assertEquals("#ops | prod", loaded.get(0).getTags().get(1));
    }
}
