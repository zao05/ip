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
                if (prevLine.equals("list")) {
                    System.out.println("     Here are the tasks in your list:");
                    for (int i = 0; i < history.size(); i++) {
                        System.out.println("     " + (i + 1) + "." + history.get(i).toString());
                    }
                } else if (prevLine.startsWith("mark")) {
                    try {
                        int index = Integer.parseInt(prevLine.substring(4).trim()) - 1;
                        Task t = history.get(index);
                        t.markAsDone();
                        System.out.println("     Nice! I've marked this task as done:");
                        System.out.println("       " + t.toString());
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new PennyException("To mark a task, please provide a valid number from your list. Try: mark 1");
                    }
                } else if (prevLine.startsWith("unmark")) {
                    try {
                        int index = Integer.parseInt(prevLine.substring(6).trim()) - 1;
                        Task t = history.get(index);
                        t.markAsUndone();
                        System.out.println("     OK, I've marked this task as not done yet:");
                        System.out.println("       " + t.toString());
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new PennyException("To unmark a task, please provide a valid number from your list. Try: unmark 1");
                    }
                } else if (prevLine.startsWith("delete")) {
                    try {
                        // Extract the index, convert to 0-based, and remove it from the ArrayList
                        int index = Integer.parseInt(prevLine.substring(6).trim()) - 1;
                        Task t = history.remove(index);
                        System.out.println("     Noted. I've removed this task:");
                        System.out.println("       " + t.toString());
                        System.out.println("     Now you have " + history.size() + " tasks in the list.");
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        throw new PennyException("To delete a task, please provide a valid number from your list. Try: delete 1");
                    }
                } else if (prevLine.startsWith("todo")) {
                    String desc = prevLine.substring(4).trim();
                    if (desc.isEmpty()) {
                        throw new PennyException("Whoops! A todo needs a description. Try: todo read a book");
                    }
                    addTask(history, new Todo(desc));
                } else if (prevLine.startsWith("deadline")) {
                    String body = prevLine.substring(8).trim();
                    if (!body.contains(" /by ")) {
                        throw new PennyException("Wait, a deadline needs a time limit. Try: deadline return book /by Sunday");
                    }
                    String[] parts = body.split(" /by ");
                    if (parts[0].trim().isEmpty() || parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new PennyException("Wait, a deadline needs both a description and a time limit. Try: deadline return book /by Sunday");
                    }
                    addTask(history, new Deadline(parts[0].trim(), parts[1].trim()));
                } else if (prevLine.startsWith("event")) {
                    String body = prevLine.substring(5).trim();
                    if (!body.contains(" /from ") || !body.contains(" /to ")) {
                        throw new PennyException("An event needs a start and end time. Try: event project meeting /from Mon 2pm /to 4pm");
                    }
                    String[] parts = body.split(" /from ");
                    String desc = parts[0].trim();
                    String[] times = parts[1].split(" /to ");
                    if (desc.isEmpty() || times.length < 2 || times[0].trim().isEmpty() || times[1].trim().isEmpty()) {
                        throw new PennyException("An event is missing details. Try: event project meeting /from Mon 2pm /to 4pm");
                    }
                    addTask(history, new Event(desc, times[0].trim(), times[1].trim()));
                } else {
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