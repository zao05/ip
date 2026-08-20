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
        String prevLine = scan.nextLine();

        while (!prevLine.equals("bye")) {
            System.out.println(divider);

            if (prevLine.equals("list")) {
                System.out.println("     Here are the tasks in your list:");
                for (int i = 0; i < history.size(); i++) {
                    System.out.println("     " + (i + 1) + "." + history.get(i).toString());
                }
            } else if (prevLine.startsWith("mark ")) {
                int index = Integer.parseInt(prevLine.substring(5).trim()) - 1;
                Task t = history.get(index);
                t.markAsDone();
                System.out.println("     Nice! I've marked this task as done:");
                System.out.println("       " + t.toString());
            } else if (prevLine.startsWith("unmark ")) {
                int index = Integer.parseInt(prevLine.substring(7).trim()) - 1;
                Task t = history.get(index);
                t.markAsUndone();
                System.out.println("     OK, I've marked this task as not done yet:");
                System.out.println("       " + t.toString());
            } else if (prevLine.startsWith("todo ")) {
                String desc = prevLine.substring(5).trim();
                addTask(history, new Todo(desc));
            } else if (prevLine.startsWith("deadline ")) {
                String body = prevLine.substring(9).trim();
                String[] parts = body.split(" /by ");
                addTask(history, new Deadline(parts[0], parts[1]));
            } else if (prevLine.startsWith("event ")) {
                String body = prevLine.substring(6).trim();
                String[] parts = body.split(" /from ");
                String desc = parts[0];
                String[] times = parts[1].split(" /to ");
                addTask(history, new Event(desc, times[0], times[1]));
            } else {
                history.add(new Task(prevLine));
                System.out.println("     added: " + prevLine);
            }

            System.out.println(divider);
            prevLine = scan.nextLine();
        }

        System.out.println(divider);
        System.out.println("     Bye. Hope to see you again soon!");
        System.out.println(divider);
        scan.close();
    }
}