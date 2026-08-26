package penny.command;

import penny.common.PennyException;
import penny.storage.Storage;
import penny.task.Task;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to delete a task by its 0-based index.
 */
public class DeleteCommand extends Command {

    private final int index;

    /**
     * Constructs a DeleteCommand with the target task index.
     *
     * @param index The 0-based index of the task to delete.
     */
    public DeleteCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException {
        Task removedTask = tasks.delete(this.index);
        ui.showTaskDeleted(removedTask, tasks.size());
        storage.save(tasks);
    }
}
