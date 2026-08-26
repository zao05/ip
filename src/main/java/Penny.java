import java.io.IOException;
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
     * Reports any I/O or security permissions issues via Ui.
     *
     * @param storage The Storage instance responsible for file persistence.
     * @param history The current list of tasks to save.
     * @param ui The Ui instance used to display error messages.
     */
    private static void saveTasks(Storage storage, List<Task> history, Ui ui) {
        try {
            storage.save(history);
        } catch (IOException e) {
            ui.showError("Warning: Failed to save tasks to file: " + e.getMessage());
        } catch (SecurityException e) {
            ui.showError("Warning: Permission denied when saving tasks to file: " + e.getMessage());
        }
    }

    private static void addTask(List<Task> history, Task t, Storage storage, Ui ui) {
        history.add(t);
        ui.showTaskAdded(t, history.size());
        saveTasks(storage, history, ui);
    }

    private static int parseTaskNumber(String command, String keyword, int listSize) throws PennyException {
        if (listSize == 0) {
            throw new PennyException("Your task list is empty. Add some tasks first!");
        }
        String argument = command.substring(keyword.length()).trim();
        if (argument.isEmpty()) {
            throw new PennyException("Please specify a task number. Try: " + keyword + " 1");
        }
        try {
            int index = Integer.parseInt(argument) - 1;
            if (index < 0 || index >= listSize) {
                throw new IndexOutOfBoundsException();
            }
            return index;
        } catch (NumberFormatException e) {
            throw new PennyException("Invalid task number: '" + argument + "'. Please enter a positive integer.");
        } catch (IndexOutOfBoundsException e) {
            throw new PennyException("Task number " + argument + " is out of range. You currently have " + listSize + " task(s).");
        }
    }

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage("data", "penny.txt");
        List<Task> history;
        try {
            history = storage.load();
        } catch (IOException e) {
            ui.showLoadingError(e.getMessage());
            history = new ArrayList<>();
        } catch (SecurityException e) {
            ui.showError("Warning: Permission denied when accessing storage file. Starting with an empty list.");
            history = new ArrayList<>();
        }

        String prevLine = ui.readCommand();

        while (!prevLine.equals("bye")) {
            ui.showLine();

            try {
                String[] inputParts = prevLine.split(" ", 2);
                CommandType command = CommandType.fromString(inputParts[0]);

                switch (command) {
                    case LIST:
                        ui.showTaskList(history);
                        break;
                    case MARK:
                        int markIndex = parseTaskNumber(prevLine, "mark", history.size());
                        Task taskToMark = history.get(markIndex);
                        taskToMark.markAsDone();
                        ui.showTaskMarked(taskToMark);
                        saveTasks(storage, history, ui);
                        break;
                    case UNMARK:
                        int unmarkIndex = parseTaskNumber(prevLine, "unmark", history.size());
                        Task taskToUnmark = history.get(unmarkIndex);
                        taskToUnmark.markAsUndone();
                        ui.showTaskUnmarked(taskToUnmark);
                        saveTasks(storage, history, ui);
                        break;
                    case DELETE:
                        int deleteIndex = parseTaskNumber(prevLine, "delete", history.size());
                        Task removedTask = history.remove(deleteIndex);
                        ui.showTaskDeleted(removedTask, history.size());
                        saveTasks(storage, history, ui);
                        break;
                    case TODO:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Whoops! A todo needs a description. Try: todo read a book");
                        }
                        String todoDesc = inputParts[1].trim();
                        if (todoDesc.contains("|")) {
                            throw new PennyException("Task description cannot contain the '|' character as it is reserved for data storage.");
                        }
                        addTask(history, new Todo(todoDesc), storage, ui);
                        break;
                    case DEADLINE:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Whoops! A deadline needs a description. Try: deadline return book /by 2019-10-15");
                        }
                        String deadlineArg = inputParts[1].trim();
                        if (!deadlineArg.contains("/by")) {
                            throw new PennyException("Wait, a deadline needs a time limit. Try: deadline return book /by 2019-10-15");
                        }
                        String[] deadlineParts = deadlineArg.split("/by", 2);
                        String deadlineDesc = deadlineParts[0].trim();
                        String deadlineBy = deadlineParts[1].trim();

                        if (deadlineDesc.isEmpty() && deadlineBy.isEmpty()) {
                            throw new PennyException("Wait, a deadline needs both a description and a time limit. Try: deadline return book /by 2019-10-15");
                        } else if (deadlineDesc.isEmpty()) {
                            throw new PennyException("Whoops! A deadline needs a description before /by. Try: deadline return book /by 2019-10-15");
                        } else if (deadlineBy.isEmpty()) {
                            throw new PennyException("Wait, a deadline needs a time limit after /by. Try: deadline return book /by 2019-10-15");
                        }
                        if (deadlineDesc.contains("|") || deadlineBy.contains("|")) {
                            throw new PennyException("Task description and deadline cannot contain the '|' character as it is reserved for data storage.");
                        }
                        addTask(history, new Deadline(deadlineDesc, deadlineBy), storage, ui);
                        break;
                    case EVENT:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Whoops! An event needs a description. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        }
                        String eventArg = inputParts[1].trim();
                        if (!eventArg.contains("/from") || !eventArg.contains("/to")) {
                            throw new PennyException("An event needs a start and end time. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        }
                        String[] eventFromParts = eventArg.split("/from", 2);
                        String eventDesc = eventFromParts[0].trim();
                        if (!eventFromParts[1].contains("/to")) {
                            throw new PennyException("An event needs a start and end time. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        }
                        String[] eventToParts = eventFromParts[1].split("/to", 2);
                        String eventFrom = eventToParts[0].trim();
                        String eventTo = eventToParts[1].trim();

                        if (eventDesc.isEmpty() && eventFrom.isEmpty() && eventTo.isEmpty()) {
                            throw new PennyException("An event is missing details. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        } else if (eventDesc.isEmpty()) {
                            throw new PennyException("Whoops! An event needs a description before /from. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        } else if (eventFrom.isEmpty()) {
                            throw new PennyException("Wait, an event needs a start time after /from. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        } else if (eventTo.isEmpty()) {
                            throw new PennyException("Wait, an event needs an end time after /to. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
                        }
                        if (eventDesc.contains("|") || eventFrom.contains("|") || eventTo.contains("|")) {
                            throw new PennyException("Task description and event times cannot contain the '|' character as it is reserved for data storage.");
                        }
                        addTask(history, new Event(eventDesc, eventFrom, eventTo), storage, ui);
                        break;
                    case ON:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Please specify a date to search for. Try: on 2019-10-15 or on 2/12/2019");
                        }
                        LocalDate targetDate = Time.parseDate(inputParts[1].trim());
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