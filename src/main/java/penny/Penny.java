package penny;

import penny.command.Command;
import penny.common.PennyException;
import penny.parser.Parser;
import penny.storage.Storage;
import penny.task.TaskList;
import penny.ui.Ui;

/**
 * Coordinates user interaction, command interpretation, task management,
 * and data persistence across the application lifecycle.
 */
public class Penny {

    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs a Penny chatbot instance with a specified storage file path.
     *
     * @param filePath The file path string to the storage file (e.g., "data/penny.txt").
     */
    public Penny(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (PennyException e) {
            ui.showLoadingError(e.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Constructs a Penny chatbot instance with OS-independent path segments.
     *
     * @param first The primary directory or path segment.
     * @param more Additional path segments if any.
     */
    public Penny(String first, String... more) {
        this.ui = new Ui();
        this.storage = new Storage(first, more);
        TaskList loadedTasks;
        try {
            loadedTasks = new TaskList(storage.load());
        } catch (PennyException e) {
            ui.showLoadingError(e.getMessage());
            loadedTasks = new TaskList();
        }
        this.tasks = loadedTasks;
    }

    /**
     * Executes the main command processing loop of the chatbot using the Command pattern.
     * Reads user commands continuously until an exit command is encountered.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (PennyException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    /**
     * Starts the Penny application from the command line.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Penny("data", "penny.txt").run();
    }
}
