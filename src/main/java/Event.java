public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    /**
     * Converts the event task into a formatted string representation suitable for file storage.
     *
     * @return Formatted string prefixed with "E | " and ending with " | [from] | [to]".
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.from + " | " + this.to;
    }
}