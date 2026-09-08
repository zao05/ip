package penny.ui;

import java.util.List;
import java.util.Scanner;

import penny.task.Task;

/**
 * Handles user interface interactions for the Penny chatbot.
 * Manages standard input from the user and formatted output to the console.
 */
public class Ui {

    private static final String DIVIDER = "____________________________________________________________";
    private static final String BANNER = " ___  ___  _  _  _  _  _  _ \n"
            + "| . \\| __>| \\| || \\| || | |\n"
            + "|  _/| _> | \\  || \\  |\\   /\n"
            + "|_|  |___>|_|\\_||_|\\_| |_| \n";
    private static final String INDENTATION = "     ";

    private final Scanner scanner;

    /**
     * Constructs a Ui object initializing the input scanner.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a line of command input from the user.
     *
     * @return The trimmed command string entered by the user.
     */
    public String readCommand() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "bye";
    }

    /**
     * Prints the standard horizontal divider line.
     */
    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Prints a message to the console prefixed with standard indentation.
     * Multi-line messages are formatted so each line aligns with the margin.
     *
     * @param message The message to print.
     */
    private void printIndented(String message) {
        System.out.println(INDENTATION + message.replace("\n", "\n" + INDENTATION));
    }

    /**
     * Formats a collection of tasks into a 1-based numbered list preceded by a header,
     * or returns the fallback message if the collection is empty.
     *
     * @param header The header text preceding the numbered items.
     * @param tasks The tasks to format into a list.
     * @param emptyMessage The fallback message to display when no tasks exist.
     * @return The formatted response string.
     */
    private String formatNumberedList(String header, List<Task> tasks, String emptyMessage) {
        if (tasks.isEmpty()) {
            printIndented(emptyMessage);
            return emptyMessage;
        }

        StringBuilder sb = new StringBuilder(header).append(":\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(".").append(tasks.get(i).toString());
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        String response = sb.toString();
        printIndented(response);
        return response;
    }

    /**
     * Displays the welcome banner and returns the greeting message.
     *
     * @return The welcome greeting string.
     */
    public String showWelcome() {
        showLine();
        System.out.println(BANNER);
        printIndented("Hello! I'm Penny.\nWhat can I do for you?");
        showLine();
        return "Hello! I'm Penny.\nWhat can I do for you?";
    }

    /**
     * Displays and returns the goodbye exit message.
     *
     * @return The exit greeting string.
     */
    public String showGoodbye() {
        String message = "Bye. Hope to see you again soon!";
        printIndented(message);
        return message;
    }

    /**
     * Displays and returns an error message with standard indentation.
     *
     * @param message The message to display.
     * @return The error message string.
     */
    public String showError(String message) {
        printIndented(message);
        return message;
    }

    /**
     * Displays and returns a warning message when initial storage loading fails.
     *
     * @param message The underlying error message.
     * @return The loading error warning string.
     */
    public String showLoadingError(String message) {
        String warning = "Warning: Could not read storage file (" + message + "). Starting with an empty list.";
        printIndented(warning);
        return warning;
    }

    /**
     * Displays and returns feedback when a task is successfully added.
     *
     * @param task The task that was added.
     * @param totalTasks The new total count of tasks.
     * @return The task added confirmation string.
     */
    public String showTaskAdded(Task task, int totalTasks) {
        String response = "Got it. I've added this task:\n"
                + "  " + task.toString() + "\n"
                + "Now you have " + totalTasks + " tasks in the list.";
        printIndented(response);
        return response;
    }

    /**
     * Displays and returns feedback when a task is marked as done.
     *
     * @param task The task that was marked done.
     * @return The marked task confirmation string.
     */
    public String showTaskMarked(Task task) {
        String response = "Nice! I've marked this task as done:\n"
                + "  " + task.toString();
        printIndented(response);
        return response;
    }

    /**
     * Displays and returns feedback when a task is marked as not yet completed.
     *
     * @param task The task that was unmarked.
     * @return The unmarked task confirmation string.
     */
    public String showTaskUnmarked(Task task) {
        String response = "OK, I've marked this task as not done yet:\n"
                + "  " + task.toString();
        printIndented(response);
        return response;
    }

    /**
     * Displays and returns feedback when a task is deleted.
     *
     * @param task The task that was removed.
     * @param remainingTasks The remaining count of tasks.
     * @return The task deleted confirmation string.
     */
    public String showTaskDeleted(Task task, int remainingTasks) {
        String response = "Noted. I've removed this task:\n"
                + "  " + task.toString() + "\n"
                + "Now you have " + remainingTasks + " tasks in the list.";
        printIndented(response);
        return response;
    }

    /**
     * Displays and returns all tasks currently in the list.
     *
     * @param tasks The list of tasks to display.
     * @return The task list string.
     */
    public String showTaskList(List<Task> tasks) {
        return formatNumberedList("Here are the tasks in your list", tasks, "Your task list is empty.");
    }

    /**
     * Displays and returns tasks matching a specific date query.
     *
     * @param matchingTasks The tasks occurring on that date.
     * @param formattedDate The formatted date string.
     * @return The matching tasks string.
     */
    public String showTasksOnDate(List<Task> matchingTasks, String formattedDate) {
        return formatNumberedList("Here are the tasks occurring on " + formattedDate,
                matchingTasks, "No tasks found occurring on " + formattedDate + ".");
    }

    /**
     * Displays and returns tasks matching a keyword search query.
     *
     * @param matchingTasks The tasks whose description matches the keyword.
     * @param keyword The search keyword.
     * @return The matching tasks string.
     */
    public String showMatchingTasks(List<Task> matchingTasks, String keyword) {
        return formatNumberedList("Here are the matching tasks in your list",
                matchingTasks, "No matching tasks found for keyword: '" + keyword + "'.");
    }

    /**
     * Closes the scanner resource.
     */
    public void close() {
        this.scanner.close();
    }
}
