/**
 * Main class for the Penny chatbot application.
 * Manages chatbot lifecycle and coordinates Ui, Storage, TaskList, and Command execution.
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
     * Executes the main command processing loop of the chatbot using the Command pattern.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (PennyException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        ui.close();
    }

    public static void main(String[] args) {
        new Penny("data/penny.txt").run();
    }
}