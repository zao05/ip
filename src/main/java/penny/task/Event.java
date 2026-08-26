package penny.task;

import java.time.LocalDate;
import penny.common.PennyException;
import penny.common.Time;

/**
 * Represents an event task occurring within a specified time window from start to end.
 */
public class Event extends Task {

    protected Time from;
    protected Time to;

    /**
     * Constructs an Event task with date/time strings for from and to boundaries.
     *
     * @param description The event description.
     * @param from The start date/time string.
     * @param to The end date/time string.
     * @throws PennyException If any date/time string cannot be parsed.
     */
    public Event(String description, String from, String to) throws PennyException {
        super(description);
        this.from = new Time(from);
        this.to = new Time(to);
    }

    /**
     * Constructs an Event task with pre-parsed Time objects for from and to.
     *
     * @param description The event description.
     * @param from The start Time.
     * @param to The end Time.
     */
    public Event(String description, Time from, Time to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public Time getFrom() {
        return this.from;
    }

    public Time getTo() {
        return this.to;
    }

    @Override
    public boolean isOnDate(LocalDate targetDate) {
        LocalDate startDate = this.from.getDate();
        LocalDate endDate = this.to.getDate();
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from.toFileFormat() + " | " + to.toFileFormat();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.toString() + " to: " + to.toString() + ")";
    }
}
