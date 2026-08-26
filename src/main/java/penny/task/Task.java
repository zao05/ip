package penny.task;

import java.time.LocalDate;

/**
 * Abstract base class representing a generic task in Penny.
 */
public abstract class Task {

    /** The description of the task. */
    protected String description;

    /** Indicates whether the task has been completed. */
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     *
     * @param description The textual description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon indicating completion.
     *
     * @return "X" if completed, " " otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done yet.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns whether the task is completed.
     *
     * @return True if completed, false otherwise.
     */
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * Checks if this task occurs on the specified date.
     * Default implementation returns false.
     *
     * @param targetDate The date to query.
     * @return True if occurring on targetDate, false otherwise.
     */
    public boolean isOnDate(LocalDate targetDate) {
        return false;
    }

    /**
     * Formats the task into a pipe-delimited string for disk persistence.
     *
     * @return Storage file formatted string.
     */
    public abstract String toFileFormat();

    /**
     * Returns the string representation of the task for display.
     *
     * @return The formatted status icon and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
