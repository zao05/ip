import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class for the Penny chatbot application.
 * Manages user interactions, command parsing, date/time task management, and file storage.
 */
public class Penny {

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");

    /**
     * Saves the current task list to disk via the storage component.
     * Reports any persistence issues via Ui.
     *
     * @param storage The Storage instance responsible for file persistence.
     * @param history The current list of tasks to save.
     * @param ui The Ui instance used to display error messages.
     */
    private static void saveTasks(Storage storage, List<Task> history, Ui ui) {
        try {
            storage.save(history);
        } catch (PennyException e) {
            ui.showError("Warning: " + e.getMessage());
        }
    }

    private static void addTask(List<Task> history, Task t, Storage storage, Ui ui) {
        history.add(t);
        ui.showTaskAdded(t, history.size());
        saveTasks(storage, history, ui);
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data", "penny.txt");
        List<Task> history;
        try {
            history = storage.load();
        } catch (PennyException e) {
            ui.showLoadingError(e.getMessage());
            history = new ArrayList<>();
        }

        String prevLine = ui.readCommand();

        while (!prevLine.equals("bye")) {
            ui.showLine();

            try {
                CommandType command = Parser.parseCommand(prevLine);
                String argsString = Parser.getArguments(prevLine);

                switch (command) {
                    case LIST:
                        ui.showTaskList(history);
                        break;
                    case MARK:
                        int markIndex = Parser.parseTaskIndex(prevLine, "mark", history.size());
                        Task taskToMark = history.get(markIndex);
                        taskToMark.markAsDone();
                        ui.showTaskMarked(taskToMark);
                        saveTasks(storage, history, ui);
                        break;
                    case UNMARK:
                        int unmarkIndex = Parser.parseTaskIndex(prevLine, "unmark", history.size());
                        Task taskToUnmark = history.get(unmarkIndex);
                        taskToUnmark.markAsUndone();
                        ui.showTaskUnmarked(taskToUnmark);
                        saveTasks(storage, history, ui);
                        break;
                    case DELETE:
                        int deleteIndex = Parser.parseTaskIndex(prevLine, "delete", history.size());
                        Task removedTask = history.remove(deleteIndex);
                        ui.showTaskDeleted(removedTask, history.size());
                        saveTasks(storage, history, ui);
                        break;
                    case TODO:
                        Todo todo = Parser.parseTodo(argsString);
                        addTask(history, todo, storage, ui);
                        break;
                    case DEADLINE:
                        Deadline deadline = Parser.parseDeadline(argsString);
                        addTask(history, deadline, storage, ui);
                        break;
                    case EVENT:
                        Event event = Parser.parseEvent(argsString);
                        addTask(history, event, storage, ui);
                        break;
                    case ON:
                        LocalDate targetDate = Parser.parseDateQuery(argsString);
                        String formattedTargetDate = targetDate.format(DISPLAY_DATE_FORMATTER);

                        List<Task> matchingTasks = new ArrayList<>();
                        for (Task task : history) {
                            if (task.isOnDate(targetDate)) {
                                matchingTasks.add(task);
                            }
                        }
                        ui.showTasksOnDate(matchingTasks, formattedTargetDate);
                        break;
                    default:
                        throw new PennyException("Hmm, I don't quite understand that command. Valid commands: todo, deadline, event, list, mark, unmark, delete, on, bye.");
                }
            } catch (PennyException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
            prevLine = ui.readCommand();
        }

        ui.showGoodbye();
        ui.close();
    }
}