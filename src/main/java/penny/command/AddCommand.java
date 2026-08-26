package penny.command;

import penny.common.PennyException;
import penny.storage.Storage;
import penny.task.Task;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to add a new task (Todo, Deadline, or Event) to the task list.
 */
public class AddCommand extends Command {

    private final Task task;

    /**
     * Constructs an AddCommand containing the task to add.
     *
     * @param task The task to be added.
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Executes the add task command, displaying feedback and saving changes to storage.
     *
     * @param tasks The task list to which the task is added.
     * @param ui The user interface used to show confirmation.
     * @param storage The storage handler used to persist the updated task list.
     * @throws PennyException If an error occurs while saving to storage.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException {
        tasks.add(this.task);
        ui.showTaskAdded(this.task, tasks.size());
        storage.save(tasks);
    }
}
