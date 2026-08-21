import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Penny {

    private static void addTask(List<Task> history, Task t) {
        history.add(t);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + t.toString());
        System.out.println("     Now you have " + history.size() + " tasks in the list.");
    }

    private static int parseTaskNumber(String command, String keyword, int listSize) throws PennyException {
        try {
            int index = Integer.parseInt(command.substring(keyword.length()).trim()) - 1;
            if (index < 0 || index >= listSize) {
                throw new IndexOutOfBoundsException();
            }
            return index;
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new PennyException("Please provide a valid task number from your list. Try: " + keyword + " 1");
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

        List<Task> history = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        String prevLine = scan.nextLine().trim();

        while (!prevLine.equals("bye")) {
            System.out.println(divider);

            try {
                String[] inputParts = prevLine.split(" ", 2);
                CommandType command = CommandType.fromString(inputParts[0]);

                switch (command) {
                    case LIST:
                        System.out.println("     Here are the tasks in your list:");
                        for (int i = 0; i < history.size(); i++) {
                            System.out.println("     " + (i + 1) + "." + history.get(i).toString());
                        }
                        break;
                    case MARK:
                        int markIndex = parseTaskNumber(prevLine, "mark", history.size());
                        Task taskToMark = history.get(markIndex);
                        taskToMark.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + taskToMark.toString());
                        break;
                    case UNMARK:
                        int unmarkIndex = parseTaskNumber(prevLine, "unmark", history.size());
                        Task taskToUnmark = history.get(unmarkIndex);
                        taskToUnmark.markAsUndone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + taskToUnmark.toString());
                        break;
                    case DELETE:
                        int deleteIndex = parseTaskNumber(prevLine, "delete", history.size());
                        Task removedTask = history.remove(deleteIndex);
                        System.out.println("     Noted. I've removed this task:");
                        System.out.println("       " + removedTask.toString());
                        System.out.println("     Now you have " + history.size() + " tasks in the list.");
                        break;
                    case TODO:
                        if (inputParts.length < 2 || inputParts[1].trim().isEmpty()) {
                            throw new PennyException("Whoops! A todo needs a description. Try: todo read a book");
                        }
                        addTask(history, new Todo(inputParts[1].trim()));
                        break;
                    case DEADLINE:
                        if (inputParts.length < 2 || !inputParts[1].contains(" /by ")) {
                            throw new PennyException("Wait, a deadline needs a time limit. Try: deadline return book /by Sunday");
                        }
                        String[] deadlineParts = inputParts[1].split(" /by ");
                        if (deadlineParts[0].trim().isEmpty() || deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
                            throw new PennyException("Wait, a deadline needs both a description and a time limit. Try: deadline return book /by Sunday");
                        }
                        addTask(history, new Deadline(deadlineParts[0].trim(), deadlineParts[1].trim()));
                        break;
                    case EVENT:
                        if (inputParts.length < 2 || !inputParts[1].contains(" /from ") || !inputParts[1].contains(" /to ")) {
                            throw new PennyException("An event needs a start and end time. Try: event project meeting /from Mon 2pm /to 4pm");
                        }
                        String[] eventParts = inputParts[1].split(" /from ");
                        String desc = eventParts[0].trim();
                        String[] times = eventParts[1].split(" /to ");
                        if (desc.isEmpty() || times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
                            throw new PennyException("An event is missing details. Try: event project meeting /from Mon 2pm /to 4pm");
                        }
                        addTask(history, new Event(desc, times[0].trim(), times[1].trim()));
                        break;
                    default:
                        throw new PennyException("Hmm, I don't quite understand that command. Valid commands: todo, deadline, event, list, mark, unmark, delete, bye.");
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