package icey.task;

import java.util.ArrayList;

import icey.IceyException;

/**
 * Manages a collection of tasks with operations for adding, removing, and querying.
 */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Validates that the given index is within bounds.
     *
     * @param index The index to validate.
     * @throws IceyException If the index is out of bounds.
     */
    public void validateIndex(int index) throws IceyException {
        if (index < 0 || index >= tasks.size()) {
            throw new IceyException("Invalid task number.");
        }
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The index of the task to remove.
     * @return The removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    public int getSize() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public ArrayList<Task> getAll() {
        return tasks;
    }

    /**
     * Counts the number of tasks that are not yet completed.
     *
     * @return The number of pending tasks.
     */
    public int countPending() {
        int count = 0;
        for (Task task : tasks) {
            if (!task.isDone()) {
                count++;
            }
        }
        return count;
    }
}
