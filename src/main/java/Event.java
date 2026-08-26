import java.time.LocalDate;

/**
 * Represents an event task with start and end times.
 */
public class Event extends Task {
    protected Time from;
    protected Time to;

    /**
     * Constructs an Event task with description and Time objects.
     *
     * @param description The task description.
     * @param from The start Time object.
     * @param to The end Time object.
     */
    public Event(String description, Time from, Time to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Constructs an Event task by parsing start and end time strings into Time objects.
     *
     * @param description The task description.
     * @param from The start time string (e.g. "2019-10-15 1400").
     * @param to The end time string (e.g. "2019-10-15 1600").
     * @throws PennyException If either date/time string cannot be parsed.
     */
    public Event(String description, String from, String to) throws PennyException {
        super(description);
        this.from = Time.parse(from);
        this.to = Time.parse(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.toString() + " to: " + to.toString() + ")";
    }

    /**
     * Converts the event task into a formatted string representation suitable for file storage.
     *
     * @return Formatted string prefixed with "E | " and ending with " | [from] | [to]".
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.from.toFileFormat() + " | " + this.to.toFileFormat();
    }

    /**
     * Checks if this event occurs on the specified date (inclusive between start date and end date).
     *
     * @param targetDate The date to check against.
     * @return True if targetDate falls between from and to dates (inclusive), false otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate targetDate) {
        if (this.from == null || this.to == null) {
            return false;
        }
        LocalDate fromDate = this.from.getDate();
        LocalDate toDate = this.to.getDate();
        return (targetDate.isEqual(fromDate) || targetDate.isAfter(fromDate))
                && (targetDate.isEqual(toDate) || targetDate.isBefore(toDate));
    }
}