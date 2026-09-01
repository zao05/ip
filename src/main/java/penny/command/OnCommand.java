package penny.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import penny.storage.Storage;
import penny.task.Task;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Represents a command to find and list all tasks occurring on a specific date.
 */
public class OnCommand extends Command {

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    private final LocalDate targetDate;

    /**
     * Constructs an OnCommand with the target date to query.
     *
     * @param targetDate The date to search for tasks.
     */
    public OnCommand(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    /**
     * Executes the date query command, filtering tasks occurring on the target date
     * and displaying them via the user interface.
     *
     * @param tasks The task list searched for matching tasks.
     * @param ui The user interface used to display matching results.
     * @param storage The storage handler (not modified by this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.findTasksOnDate(this.targetDate);
        String formattedDate = this.targetDate.format(DISPLAY_DATE_FORMATTER);
        ui.showTasksOnDate(matchingTasks, formattedDate);
    }
}
