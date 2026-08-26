package penny.parser;

/**
 * Enumeration representing valid command keywords supported by Penny.
 */
public enum CommandType {
    /** Command to add a todo task. */
    TODO,

    /** Command to add a deadline task. */
    DEADLINE,

    /** Command to add an event task. */
    EVENT,

    /** Command to list all tasks. */
    LIST,

    /** Command to mark a task as completed. */
    MARK,

    /** Command to unmark a task as incomplete. */
    UNMARK,

    /** Command to delete a task. */
    DELETE,

    /** Command to filter tasks occurring on a specific date. */
    ON,

    /** Command to exit the application. */
    BYE,

    /** Represents an unrecognized or invalid command keyword. */
    UNKNOWN;

    /**
     * Parses a string input into its corresponding CommandType.
     *
     * @param command The string keyword to match.
     * @return The matched CommandType, or UNKNOWN if not found.
     */
    public static CommandType fromString(String command) {
        if (command == null || command.trim().isEmpty()) {
            return UNKNOWN;
        }
        try {
            return CommandType.valueOf(command.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
