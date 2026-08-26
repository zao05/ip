package penny.command;

import penny.storage.Storage;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to exit the Penny application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
