package icey.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskTest {

    @Test
    public void markAsDone_togglesStatus() {
        Todo todo = new Todo("test");
        assertFalse(todo.isDone());
        todo.markAsDone();
        assertTrue(todo.isDone());
        todo.markAsNotDone();
        assertFalse(todo.isDone());
    }

    @Test
    public void toString_todo_formatsCorrectly() {
        Todo todo = new Todo("borrow book");
        assertEquals("[T][ ] borrow book", todo.toString());
        todo.markAsDone();
        assertEquals("[T][X] borrow book", todo.toString());
    }

    @Test
    public void toString_deadline_formatsCorrectly() {
        LocalDateTime by = LocalDateTime.of(2019, 12, 2, 18, 0);
        Deadline deadline = new Deadline("return book", by);
        assertEquals("[D][ ] return book (by: Dec 02 2019 6:00 PM)", deadline.toString());
    }

    @Test
    public void toString_event_formatsCorrectly() {
        LocalDateTime from = LocalDateTime.of(2019, 12, 2, 14, 0);
        LocalDateTime to = LocalDateTime.of(2019, 12, 2, 16, 0);
        Event event = new Event("meeting", from, to);
        assertEquals("[E][ ] meeting (from: Dec 02 2019 2:00 PM to: Dec 02 2019 4:00 PM)", event.toString());
    }
}
