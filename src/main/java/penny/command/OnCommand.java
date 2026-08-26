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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        List<Task> matchingTasks = tasks.findTasksOnDate(this.targetDate);
        String formattedDate = this.targetDate.format(DISPLAY_DATE_FORMATTER);
        ui.showTasksOnDate(matchingTasks, formattedDate);
    }
}
