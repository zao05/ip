public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + this.description;
    }

    /**
     * Converts the task into a formatted string representation suitable for file storage.
     *
     * @return Formatted string containing the completion status (1 for done, 0 for undone) and description.
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + this.description;
    }
}