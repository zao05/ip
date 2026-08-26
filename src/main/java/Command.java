/**
 * Represents an executable command in the Penny application.
 * Defines the contract for executing user commands with access to TaskList, Ui, and Storage.
 */
public abstract class Command {

    /**
     * Executes the command.
     *
     * @param tasks The task list manipulated by the command.
     * @param ui The user interface for displaying feedback.
     * @param storage The storage handler for saving tasks.
     * @throws PennyException If execution fails due to invalid parameters or state.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws PennyException;

    /**
     * Indicates whether this command signals the chatbot to exit.
     *
     * @return True if this is an exit command, false otherwise.
     */
    public boolean isExit() {
        return false;
    }
}
