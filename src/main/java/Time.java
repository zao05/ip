import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Encapsulates and parses date and time values for tasks.
 * Supports flexible input parsing, standard formatted display, and disk persistence.
 */
public class Time {

    private static final List<DateTimeFormatter> DATE_TIME_INPUT_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mma"),
            DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a"),
            DateTimeFormatter.ISO_LOCAL_DATE_TIME
    );

    private static final List<DateTimeFormatter> DATE_INPUT_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("MMM d yyyy"),
            DateTimeFormatter.ISO_LOCAL_DATE
    );

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");

    private static final DateTimeFormatter STORAGE_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter STORAGE_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    private final LocalDate date;
    private final LocalTime time; // Optional (null if only date is specified)

    /**
     * Constructs a Time object with a date and optional time.
     *
     * @param date The date component.
     * @param time The time component (can be null).
     */
    public Time(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Constructs a Time object with only a date.
     *
     * @param date The date component.
     */
    public Time(LocalDate date) {
        this(date, null);
    }

    /**
     * Parses a date/time string into a Time instance.
     *
     * @param input The input string containing a date (and optional time).
     * @return A parsed Time object.
     * @throws PennyException If the string cannot be parsed into any recognized date/time format.
     */
    public static Time parse(String input) throws PennyException {
        if (input == null || input.trim().isEmpty()) {
            throw new PennyException("Date/time cannot be empty.");
        }

        String trimmed = input.trim();

        // 1. Try parsing as date with time
        for (DateTimeFormatter formatter : DATE_TIME_INPUT_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(trimmed, formatter);
                return new Time(dateTime.toLocalDate(), dateTime.toLocalTime());
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }

        // 2. Try parsing as date only
        for (DateTimeFormatter formatter : DATE_INPUT_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return new Time(date, null);
            } catch (DateTimeParseException ignored) {
                // Try next formatter
            }
        }

        throw new PennyException("Invalid date/time format: '" + trimmed
                + "'. Please use formats like yyyy-MM-dd (e.g. 2019-10-15) or d/M/yyyy HHmm (e.g. 2/12/2019 1800).");
    }

    /**
     * Parses a date string into a LocalDate instance.
     *
     * @param input The input string containing a date.
     * @return The parsed LocalDate.
     * @throws PennyException If the string cannot be parsed as a valid date.
     */
    public static LocalDate parseDate(String input) throws PennyException {
        return parse(input).getDate();
    }

    /**
     * Returns the date component of this Time.
     *
     * @return The LocalDate component.
     */
    public LocalDate getDate() {
        return this.date;
    }

    /**
     * Checks if this Time matches a specific date.
     *
     * @param targetDate The target date to check against.
     * @return True if the date component equals targetDate, false otherwise.
     */
    public boolean isOnDate(LocalDate targetDate) {
        return this.date != null && this.date.equals(targetDate);
    }

    /**
     * Formats the date/time for user display.
     * Example: "Oct 15 2019" or "Dec 2 2019, 6:00PM".
     *
     * @return Formatted string representation for display.
     */
    @Override
    public String toString() {
        if (this.time == null) {
            return this.date.format(DISPLAY_DATE_FORMATTER);
        }
        return LocalDateTime.of(this.date, this.time).format(DISPLAY_DATE_TIME_FORMATTER);
    }

    /**
     * Formats the date/time for persistent disk storage.
     * Example: "2019-10-15" or "2019-12-02 1800".
     *
     * @return Storage formatted string representation.
     */
    public String toFileFormat() {
        if (this.time == null) {
            return this.date.format(STORAGE_DATE_FORMATTER);
        }
        return LocalDateTime.of(this.date, this.time).format(STORAGE_DATE_TIME_FORMATTER);
    }
}
