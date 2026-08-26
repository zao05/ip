import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Main class for the Penny chatbot application.
 * Manages chatbot lifecycle and coordinates Ui, Storage, TaskList, and Parser components.
 */
public class Penny {

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");

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
     * Executes the main command processing loop of the chatbot.
     */
    public void run() {
        ui.showWelcome();
        String commandString = ui.readCommand();

        while (!commandString.equals("bye")) {
            ui.showLine();

            try {
                CommandType command = Parser.parseCommand(commandString);
                String argsString = Parser.getArguments(commandString);

                switch (command) {
                    case LIST:
                        ui.showTaskList(tasks.getAllTasks());
                        break;
                    case MARK:
                        int markIndex = Parser.parseTaskIndex(commandString, "mark", tasks.size());
                        Task taskToMark = tasks.mark(markIndex);
                        ui.showTaskMarked(taskToMark);
                        saveTasks();
                        break;
                    case UNMARK:
                        int unmarkIndex = Parser.parseTaskIndex(commandString, "unmark", tasks.size());
                        Task taskToUnmark = tasks.unmark(unmarkIndex);
                        ui.showTaskUnmarked(taskToUnmark);
                        saveTasks();
                        break;
                    case DELETE:
                        int deleteIndex = Parser.parseTaskIndex(commandString, "delete", tasks.size());
                        Task removedTask = tasks.delete(deleteIndex);
                        ui.showTaskDeleted(removedTask, tasks.size());
                        saveTasks();
                        break;
                    case TODO:
                        Todo todo = Parser.parseTodo(argsString);
                        tasks.add(todo);
                        ui.showTaskAdded(todo, tasks.size());
                        saveTasks();
                        break;
                    case DEADLINE:
                        Deadline deadline = Parser.parseDeadline(argsString);
                        tasks.add(deadline);
                        ui.showTaskAdded(deadline, tasks.size());
                        saveTasks();
                        break;
                    case EVENT:
                        Event event = Parser.parseEvent(argsString);
                        tasks.add(event);
                        ui.showTaskAdded(event, tasks.size());
                        saveTasks();
                        break;
                    case ON:
                        LocalDate targetDate = Parser.parseDateQuery(argsString);
                        String formattedDate = targetDate.format(DISPLAY_DATE_FORMATTER);
                        List<Task> matchingTasks = tasks.findTasksOnDate(targetDate);
                        ui.showTasksOnDate(matchingTasks, formattedDate);
                        break;
                    default:
                        throw new PennyException("Hmm, I don't quite understand that command. Valid commands: todo, deadline, event, list, mark, unmark, delete, on, bye.");
                }
            } catch (PennyException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
            commandString = ui.readCommand();
        }

        ui.showGoodbye();
        ui.close();
    }

    /**
     * Saves the current tasks to disk, reporting any error via Ui.
     */
    private void saveTasks() {
        try {
            storage.save(tasks);
        } catch (PennyException e) {
            ui.showError("Warning: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Penny("data", "penny.txt").run();
    }
}