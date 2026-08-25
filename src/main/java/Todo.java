public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Converts the todo task into a formatted string representation suitable for file storage.
     *
     * @return Formatted string prefixed with "T | ".
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}
