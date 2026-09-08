package penny.command;

import penny.storage.Storage;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to list all tasks in the task list.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by requesting the user interface to display all tasks.
     *
     * @param tasks The task list whose contents will be displayed.
     * @param ui The user interface used to render the task list.
     * @param storage The storage handler (not modified by this command).
     * @return The task list string from the user interface.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        return ui.showTaskList(tasks.getAllTasks());
    }
}
