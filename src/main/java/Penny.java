import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
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
     * Catches and reports any I/O or security permissions issues.
     *
     * @param storage The Storage instance responsible for file persistence.
     * @param history The current list of tasks to save.
     */
    private static void saveTasks(Storage storage, List<Task> history) {
        try {
            storage.save(history);
        } catch (IOException e) {
            System.out.println("     Warning: Failed to save tasks to file: " + e.getMessage());
        } catch (SecurityException e) {
            System.out.println("     Warning: Permission denied when saving tasks to file: " + e.getMessage());
        }
    }

    private static void addTask(List<Task> history, Task t, Storage storage) {
        history.add(t);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + t.toString());
        System.out.println("     Now you have " + history.size() + " tasks in the list.");
        saveTasks(storage, history);
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
        String divider = "____________________________________________________________";
        String banner = " ___  ___  _  _  _  _  _  _ \n"
                + "| . \\| __>| \\| || \\| || | |\n"
                + "|  _/| _> | \\  || \\  |\\   /\n"
                + "|_|  |___>|_|\\_||_|\\_| |_| \n";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println("     Hello! I'm Penny.");
        System.out.println("     What can I do for you?");
        System.out.println(divider);

        Storage storage = new Storage("data", "penny.txt");
        List<Task> history;
        try {
            history = storage.load();
        } catch (IOException e) {
            System.out.println("     Warning: Could not read storage file (" + e.getMessage() + "). Starting with an empty list.");
            history = new ArrayList<>();
        } catch (SecurityException e) {
            System.out.println("     Warning: Permission denied when accessing storage file. Starting with an empty list.");
            history = new ArrayList<>();
        }

        Scanner scan = new Scanner(System.in);
        String prevLine = scan.nextLine().trim();

        while (!prevLine.equals("bye")) {
            System.out.println(divider);

            try {
                String[] inputParts = prevLine.split(" ", 2);
                CommandType command = CommandType.fromString(inputParts[0]);

                switch (command) {
                    case LIST:
                        if (history.isEmpty()) {
                            System.out.println("     Your task list is empty.");
                        } else {
                            System.out.println("     Here are the tasks in your list:");
                            for (int i = 0; i < history.size(); i++) {
                                System.out.println("     " + (i + 1) + "." + history.get(i).toString());
                            }
                        }
                        break;
                    case MARK:
                        int markIndex = parseTaskNumber(prevLine, "mark", history.size());
                        Task taskToMark = history.get(markIndex);
                        taskToMark.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + taskToMark.toString());
                        saveTasks(storage, history);
                        break;
                    case UNMARK:
                        int unmarkIndex = parseTaskNumber(prevLine, "unmark", history.size());
                        Task taskToUnmark = history.get(unmarkIndex);
                        taskToUnmark.markAsUndone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + taskToUnmark.toString());
                        saveTasks(storage, history);
                        break;
                    case DELETE:
                        int deleteIndex = parseTaskNumber(prevLine, "delete", history.size());
                        Task removedTask = history.remove(deleteIndex);
                        System.out.println("     Noted. I've removed this task:");
                        System.out.println("       " + removedTask.toString());
                        System.out.println("     Now you have " + history.size() + " tasks in the list.");
                        saveTasks(storage, history);
                        break;
                    case TODO:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Whoops! A todo needs a description. Try: todo read a book");
                        }
                        String todoDesc = inputParts[1].trim();
                        if (todoDesc.contains("|")) {
                            throw new PennyException("Task description cannot contain the '|' character as it is reserved for data storage.");
                        }
                        addTask(history, new Todo(todoDesc), storage);
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
                        addTask(history, new Deadline(deadlineDesc, deadlineBy), storage);
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
                        addTask(history, new Event(eventDesc, eventFrom, eventTo), storage);
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

                        if (matchingTasks.isEmpty()) {
                            System.out.println("     No tasks found occurring on " + formattedTargetDate + ".");
                        } else {
                            System.out.println("     Here are the tasks occurring on " + formattedTargetDate + ":");
                            for (int i = 0; i < matchingTasks.size(); i++) {
                                System.out.println("     " + (i + 1) + "." + matchingTasks.get(i).toString());
                            }
                        }
                        break;
                    default:
                        throw new PennyException("Hmm, I don't quite understand that command. Valid commands: todo, deadline, event, list, mark, unmark, delete, on, bye.");
                }
            } catch (PennyException e) {
                System.out.println("     " + e.getMessage());
            }

            System.out.println(divider);
            prevLine = scan.nextLine().trim();
        }

        System.out.println(divider);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(divider);
        scan.close();
    }
}