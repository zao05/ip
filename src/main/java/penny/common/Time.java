package penny.common;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Handles parsing and formatting of dates and optional times for tasks in Penny.
 * Encapsulates a {@link LocalDate} and optional {@link LocalTime}.
 */
public class Time {

    private static final List<DateTimeFormatter> DATE_TIME_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")
    );

    private static final List<DateTimeFormatter> DATE_ONLY_FORMATTERS = List.of(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("d/M/yyyy")
    );

    private static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    private static final DateTimeFormatter DISPLAY_TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mma");

    private final LocalDate date;
    private final LocalTime time;

    /**
     * Constructs a Time instance by parsing a date (and optional time) string.
     *
     * @param input The date/time string to parse.
     * @throws PennyException If the input format does not match any accepted pattern.
     */
    public Time(String input) throws PennyException {
        String trimmed = input.trim();
        LocalDate parsedDate = null;
        LocalTime parsedTime = null;
        boolean isParsed = false;

        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(trimmed, formatter);
                parsedDate = ldt.toLocalDate();
                parsedTime = ldt.toLocalTime();
                isParsed = true;
                break;
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }

        if (!isParsed) {
            for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
                try {
                    parsedDate = LocalDate.parse(trimmed, formatter);
                    parsedTime = null;
                    isParsed = true;
                    break;
                } catch (DateTimeParseException ignored) {
                    // Try next pattern
                }
            }
        }

        if (!isParsed) {
            throw new PennyException("Invalid date format: '" + input
                    + "'. Please use yyyy-MM-dd or d/M/yyyy (e.g. 2019-10-15 or 2/12/2019 1800).");
        }

        this.date = parsedDate;
        this.time = parsedTime;
    }

    /**
     * Parses a date string for search queries where time is not required.
     *
     * @param input The date string to parse.
     * @return The parsed {@link LocalDate}.
     * @throws PennyException If the input format is invalid.
     */
    public static LocalDate parseDate(String input) throws PennyException {
        String trimmed = input.trim();
        for (DateTimeFormatter formatter : DATE_ONLY_FORMATTERS) {
            try {
                return LocalDate.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter).toLocalDate();
            } catch (DateTimeParseException ignored) {
                // Try next pattern
            }
        }
        throw new PennyException("Invalid date format: '" + input
                + "'. Please use yyyy-MM-dd or d/M/yyyy (e.g. 2019-10-15 or 2/12/2019).");
    }

    public LocalDate getDate() {
        return this.date;
    }

    public boolean hasTime() {
        return this.time != null;
    }

    public LocalTime getTime() {
        return this.time;
    }

    public boolean isOnDate(LocalDate targetDate) {
        return this.date.equals(targetDate);
    }

    public String toFileFormat() {
        if (this.time != null) {
            return this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " "
                    + this.time.format(DateTimeFormatter.ofPattern("HHmm"));
        }
        return this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public String toString() {
        if (this.time != null) {
            return this.date.format(DISPLAY_DATE_FORMATTER) + ", "
                    + this.time.format(DISPLAY_TIME_FORMATTER).toLowerCase();
        }
        return this.date.format(DISPLAY_DATE_FORMATTER);
    }
}
