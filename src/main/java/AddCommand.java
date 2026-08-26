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
