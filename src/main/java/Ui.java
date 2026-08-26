import java.util.List;
import java.util.Scanner;

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
     * Displays the welcome banner and greeting message.
     */
    public void showWelcome() {
        showLine();
        System.out.println(BANNER);
        System.out.println("     Hello! I'm Penny.");
        System.out.println("     What can I do for you?");
        showLine();
    }

    /**
     * Displays the goodbye exit message.
     */
    public void showGoodbye() {
        System.out.println("     Bye. Hope to see you again soon!");
    }

    /**
     * Displays an error or warning message with standard indentation.
     *
     * @param message The message to display.
     */
    public void showError(String message) {
        System.out.println("     " + message);
    }

    /**
     * Displays a warning message when initial storage loading fails.
     *
     * @param message The underlying error message.
     */
    public void showLoadingError(String message) {
        System.out.println("     Warning: Could not read storage file (" + message + "). Starting with an empty list.");
    }

    /**
     * Displays feedback when a task is successfully added.
     *
     * @param task The task that was added.
     * @param totalTasks The new total count of tasks.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task.toString());
        System.out.println("     Now you have " + totalTasks + " tasks in the list.");
    }

    /**
     * Displays feedback when a task is marked as done.
     *
     * @param task The task that was marked done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("     Nice! I've marked this task as done:");
        System.out.println("       " + task.toString());
    }

    /**
     * Displays feedback when a task is marked as not yet completed.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("     OK, I've marked this task as not done yet:");
        System.out.println("       " + task.toString());
    }

    /**
     * Displays feedback when a task is deleted.
     *
     * @param task The task that was removed.
     * @param remainingTasks The remaining count of tasks.
     */
    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task.toString());
        System.out.println("     Now you have " + remainingTasks + " tasks in the list.");
    }

    /**
     * Displays all tasks currently in the list.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("     Your task list is empty.");
        } else {
            System.out.println("     Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + tasks.get(i).toString());
            }
        }
    }

    /**
     * Displays tasks matching a specific date query.
     *
     * @param matchingTasks The tasks occurring on that date.
     * @param formattedDate The formatted date string.
     */
    public void showTasksOnDate(List<Task> matchingTasks, String formattedDate) {
        if (matchingTasks.isEmpty()) {
            System.out.println("     No tasks found occurring on " + formattedDate + ".");
        } else {
            System.out.println("     Here are the tasks occurring on " + formattedDate + ":");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println("     " + (i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
    }

    /**
     * Closes the scanner resource.
     */
    public void close() {
        this.scanner.close();
    }
}
