package penny.command;

import penny.common.PennyException;
import penny.storage.Storage;
import penny.task.Task;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to add a new task (Todo, Deadline, or Event).
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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException {
        tasks.add(this.task);
        ui.showTaskAdded(this.task, tasks.size());
        storage.save(tasks);
    }
}
