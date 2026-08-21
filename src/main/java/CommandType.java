public enum CommandType {
    TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE, UNKNOWN;

    /**
     * Converts a string into the corresponding CommandType.
     * @param commandWord The first word of the user's input.
     * @return The matching CommandType, or UNKNOWN if it doesn't match any.
     */
    public static CommandType fromString(String commandWord) {
        try {
            return CommandType.valueOf(commandWord.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}