package icey.command;

import icey.IceyException;
import icey.Storage;
import icey.task.Task;
import icey.task.TaskList;
import icey.ui.Ui;

/**
 * Command to add a tag to a task.
 */
public class TagCommand extends Command {
    private final int index;
    private final String tag;

    /**
     * Creates a new TagCommand for the specified task index and tag.
     *
     * @param index The index of the task to tag.
     * @param tag   The tag to add.
     */
    public TagCommand(int index, String tag) {
        this.index = index;
        this.tag = tag;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IceyException {
        tasks.validateIndex(index);
        Task task = tasks.get(index);
        if (!task.addTag(tag)) {
            throw new IceyException("Task already has tag " + tag + ".");
        }
        storage.save(tasks);
        ui.showMessages("Fox tag pinned:", ui.getIndent() + task.toString());
    }
}
