package icey;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import icey.task.TaskList;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void load_corruptedLinesPresent_loadsValidLinesOnly() throws Exception {
        Path dataFile = tempDir.resolve("icey.txt");
        Files.write(dataFile, List.of(
                "T | 0 | borrow book",
                "BAD LINE",
                "D | 1 | return book | 2019-12-02T18:00:00",
                "E | 0 | meeting | invalid-from | 2019-12-02T16:00:00"
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
}
