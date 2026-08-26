import java.time.LocalDate;

/**
 * Represents a task with a deadline date/time.
 */
public class Deadline extends Task {
    protected Time by;

    /**
     * Constructs a Deadline task with a description and a Time object.
     *
     * @param description The task description.
     * @param by The deadline Time object.
     */
    public Deadline(String description, Time by) {
        super(description);
        this.by = by;
    }

    /**
     * Constructs a Deadline task by parsing the deadline string into a Time object.
     *
     * @param description The task description.
     * @param by The deadline string (e.g., "2019-10-15" or "2/12/2019 1800").
     * @throws PennyException If the date/time string cannot be parsed.
     */
    public Deadline(String description, String by) throws PennyException {
        super(description);
        this.by = Time.parse(by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.toString() + ")";
    }

    /**
     * Converts the deadline task into a formatted string representation suitable for file storage.
     *
     * @return Formatted string prefixed with "D | " and ending with " | [by]".
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + this.by.toFileFormat();
    }

    /**
     * Checks if this deadline occurs on the specified date.
     *
     * @param targetDate The date to check against.
     * @return True if the deadline date matches targetDate, false otherwise.
     */
    @Override
    public boolean isOnDate(LocalDate targetDate) {
        return this.by != null && this.by.isOnDate(targetDate);
    }
}