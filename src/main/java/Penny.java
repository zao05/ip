import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Penny {
    public static void main(String[] args) {

        String divider = "____________________________________________________________";

        String banner = " ___  ___  _  _  _  _  _  _ \n"
                + "| . \\| __>| \\| || \\| || | |\n"
                + "|  _/| _> | \\  || \\  |\\   /\n"
                + "|_|  |___>|_|\\_||_|\\_| |_| \n";

        System.out.println(divider);
        System.out.println(banner);
        System.out.println("Hello! I'm Penny.");
        System.out.println("What can I do for you?");
        System.out.println(divider);
        List<String> history = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        String prevLine = scan.nextLine();
        while (! prevLine.equals("bye")) {
            System.out.println(divider);
            if (prevLine.equals("list")) {
                IntStream.range(1, history.size() + 1)
                        .forEach(i -> System.out.println(i + ". " + history.get(i - 1)));

            }
            else {
                history.add(prevLine);
                System.out.println("added: " + prevLine);

            }
            System.out.println(divider);
            prevLine = scan.nextLine();
        }
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(divider);
    }
}