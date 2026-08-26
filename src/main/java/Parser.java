import java.time.LocalDate;

/**
 * Deals with making sense of user commands.
 * Parses raw text input into executable Command objects.
 */
public class Parser {

    /**
     * Parses the full user command string and returns the corresponding executable Command.
     *
     * @param fullCommand The raw line of text entered by the user.
     * @return The executable Command object.
     * @throws PennyException If the command word is unknown or the arguments are malformed.
     */
    public static Command parse(String fullCommand) throws PennyException {
        String trimmed = fullCommand.trim();
        String[] parts = trimmed.split(" ", 2);
        String commandWord = parts[0].toLowerCase();
        String args = parts.length > 1 ? parts[1].trim() : "";

        switch (commandWord) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark":
            return new MarkCommand(parseTaskIndex(fullCommand, "mark"));
        case "unmark":
            return new UnmarkCommand(parseTaskIndex(fullCommand, "unmark"));
        case "delete":
            return new DeleteCommand(parseTaskIndex(fullCommand, "delete"));
        case "todo":
            return new AddCommand(parseTodo(args));
        case "deadline":
            return new AddCommand(parseDeadline(args));
        case "event":
            return new AddCommand(parseEvent(args));
        case "on":
            return new OnCommand(parseDateQuery(args));
        default:
            throw new PennyException("Hmm, I don't quite understand that command. Valid commands: todo, deadline, event, list, mark, unmark, delete, on, bye.");
        }
    }

    /**
     * Parses a 0-based task index from commands like "mark 2", "unmark 1", "delete 3".
     *
     * @param fullCommand The raw command string.
     * @param keyword The command keyword (e.g., "mark", "unmark", "delete").
     * @return The 0-based index of the target task.
     * @throws PennyException If the task number is missing or non-numeric.
     */
    public static int parseTaskIndex(String fullCommand, String keyword) throws PennyException {
        String argument = fullCommand.substring(keyword.length()).trim();
        if (argument.isEmpty()) {
            throw new PennyException("Please specify a task number. Try: " + keyword + " 1");
        }
        try {
            int index = Integer.parseInt(argument) - 1;
            if (index < 0) {
                throw new PennyException("Invalid task number: '" + argument + "'. Please enter a positive integer.");
            }
            return index;
        } catch (NumberFormatException e) {
            throw new PennyException("Invalid task number: '" + argument + "'. Please enter a positive integer.");
        }
    }

    /**
     * Parses arguments for a todo command and constructs a Todo task.
     *
     * @param args The argument string after "todo".
     * @return A new Todo task.
     * @throws PennyException If description is empty or contains reserved delimiter '|'.
     */
    public static Todo parseTodo(String args) throws PennyException {
        if (args.isEmpty()) {
            throw new PennyException("Whoops! A todo needs a description. Try: todo read a book");
        }
        if (args.contains("|")) {
            throw new PennyException("Task description cannot contain the '|' character as it is reserved for data storage.");
        }
        return new Todo(args);
    }

    /**
     * Parses arguments for a deadline command and constructs a Deadline task.
     *
     * @param args The argument string after "deadline".
     * @return A new Deadline task.
     * @throws PennyException If description or deadline time is missing or invalid.
     */
    public static Deadline parseDeadline(String args) throws PennyException {
        if (args.isEmpty()) {
            throw new PennyException("Whoops! A deadline needs a description. Try: deadline return book /by 2019-10-15");
        }
        if (!args.contains("/by")) {
            throw new PennyException("Wait, a deadline needs a time limit. Try: deadline return book /by 2019-10-15");
        }
        String[] parts = args.split("/by", 2);
        String desc = parts[0].trim();
        String by = parts[1].trim();

        if (desc.isEmpty() && by.isEmpty()) {
            throw new PennyException("Wait, a deadline needs both a description and a time limit. Try: deadline return book /by 2019-10-15");
        } else if (desc.isEmpty()) {
            throw new PennyException("Whoops! A deadline needs a description before /by. Try: deadline return book /by 2019-10-15");
        } else if (by.isEmpty()) {
            throw new PennyException("Wait, a deadline needs a time limit after /by. Try: deadline return book /by 2019-10-15");
        }
        if (desc.contains("|") || by.contains("|")) {
            throw new PennyException("Task description and deadline cannot contain the '|' character as it is reserved for data storage.");
        }
        return new Deadline(desc, by);
    }

    /**
     * Parses arguments for an event command and constructs an Event task.
     *
     * @param args The argument string after "event".
     * @return A new Event task.
     * @throws PennyException If description, start time, or end time is missing or invalid.
     */
    public static Event parseEvent(String args) throws PennyException {
        if (args.isEmpty()) {
            throw new PennyException("Whoops! An event needs a description. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        }
        if (!args.contains("/from") || !args.contains("/to")) {
            throw new PennyException("An event needs a start and end time. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        }
        String[] fromParts = args.split("/from", 2);
        String desc = fromParts[0].trim();
        if (!fromParts[1].contains("/to")) {
            throw new PennyException("An event needs a start and end time. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        }
        String[] toParts = fromParts[1].split("/to", 2);
        String from = toParts[0].trim();
        String to = toParts[1].trim();

        if (desc.isEmpty() && from.isEmpty() && to.isEmpty()) {
            throw new PennyException("An event is missing details. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        } else if (desc.isEmpty()) {
            throw new PennyException("Whoops! An event needs a description before /from. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        } else if (from.isEmpty()) {
            throw new PennyException("Wait, an event needs a start time after /from. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        } else if (to.isEmpty()) {
            throw new PennyException("Wait, an event needs an end time after /to. Try: event project meeting /from 2019-10-15 1400 /to 2019-10-15 1600");
        }
        if (desc.contains("|") || from.contains("|") || to.contains("|")) {
            throw new PennyException("Task description and event times cannot contain the '|' character as it is reserved for data storage.");
        }
        return new Event(desc, from, to);
    }

    /**
     * Parses the date argument for the 'on' date search command.
     *
     * @param args The date argument string.
     * @return The parsed LocalDate.
     * @throws PennyException If date argument is empty or invalid.
     */
    public static LocalDate parseDateQuery(String args) throws PennyException {
        if (args.isEmpty()) {
            throw new PennyException("Please specify a date to search for. Try: on 2019-10-15 or on 2/12/2019");
        }
        return Time.parseDate(args);
    }
}
