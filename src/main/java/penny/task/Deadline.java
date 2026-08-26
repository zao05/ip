package penny.task;

import java.time.LocalDate;
import penny.common.PennyException;
import penny.common.Time;

/**
 * Represents a task that needs to be completed by a specific date and time.
 */
public class Deadline extends Task {

    protected Time by;

    /**
     * Constructs a Deadline task with a description and a by-date string.
     *
     * @param description The deadline description.
     * @param by The deadline date/time string.
     * @throws PennyException If the date/time format is invalid.
     */
    public Deadline(String description, String by) throws PennyException {
        super(description);
        this.by = new Time(by);
    }

    /**
     * Constructs a Deadline task with a description and a pre-parsed Time object.
     *
     * @param description The deadline description.
     * @param by The pre-parsed Time object.
     */
    public Deadline(String description, Time by) {
        super(description);
        this.by = by;
    }

    public Time getBy() {
        return this.by;
    }

    @Override
    public boolean isOnDate(LocalDate targetDate) {
        return this.by.isOnDate(targetDate);
    }

    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by.toFileFormat();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.toString() + ")";
    }
}
