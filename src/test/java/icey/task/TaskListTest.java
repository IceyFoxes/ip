package icey.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskListTest {
    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void addAndGet_task_works() {
        Todo todo = new Todo("test task");
        taskList.add(todo);
        assertEquals(1, taskList.getSize());
        assertEquals(todo, taskList.get(0));
    }

    @Test
    public void remove_task_decreasesSize() {
        taskList.add(new Todo("task 1"));
        taskList.add(new Todo("task 2"));
        Task removed = taskList.remove(0);
        assertEquals(1, taskList.getSize());
        assertEquals("task 1", removed.getDescription());
    }

    @Test
    public void countPending_mixedTasks_countsCorrectly() {
        Todo done = new Todo("done task");
        done.markAsDone();
        taskList.add(done);
        taskList.add(new Todo("pending 1"));
        taskList.add(new Todo("pending 2"));
        assertEquals(2, taskList.countPending());
    }

    @Test
    public void isEmpty_emptyList_returnsTrue() {
        assertTrue(taskList.isEmpty());
    }
}
