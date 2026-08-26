/**
 * Represents the set of valid commands recognized by the Penny chatbot.
 */
public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, ON, BYE, UNKNOWN;

    /**
     * Converts a command word string into the corresponding CommandType.
     *
     * @param commandWord The first word of the user's input.
     * @return The matching CommandType, or UNKNOWN if no match is found.
     */
    public static CommandType fromString(String commandWord) {
        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}