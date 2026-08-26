package penny.command;

import penny.common.PennyException;
import penny.storage.Storage;
import penny.task.Task;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to mark a task as not yet completed.
 */
public class UnmarkCommand extends Command {

    private final int index;

    /**
     * Constructs an UnmarkCommand with the target task index.
     *
     * @param index The 0-based index of the task to unmark.
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException {
        Task task = tasks.unmark(this.index);
        ui.showTaskUnmarked(task);
        storage.save(tasks);
    }
}
