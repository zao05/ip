/**
 * Represents a command to mark a task as completed.
 */
public class MarkCommand extends Command {

    private final int index;

    /**
     * Constructs a MarkCommand with the target task index.
     *
     * @param index The 0-based index of the task to mark done.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException {
        Task task = tasks.mark(this.index);
        ui.showTaskMarked(task);
        storage.save(tasks);
    }
}
