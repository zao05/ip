import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles persistent storage of task data on the hard disk.
 * Provides robust functionality to save tasks to and load tasks from a designated file,
 * gracefully handling non-existent files, OS-independent relative paths, and corrupted entries.
 */
public class Storage {
    private final Path filePath;

    /**
     * Constructs a Storage object configured with an OS-independent relative path constructed from path segments.
     * For example, {@code new Storage("data", "penny.txt")} resolves to {@code data/penny.txt} on Unix/macOS
     * and {@code data\penny.txt} on Windows.
     *
     * @param first The primary directory or path segment.
     * @param more Additional path segments if any.
     */
    public Storage(String first, String... more) {
        this.filePath = Paths.get(first, more);
    }

    /**
     * Constructs a Storage object with a specified {@link Path}.
     *
     * @param filePath The {@link Path} where tasks will be stored.
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to the storage file on the hard disk.
     * Creates any missing parent directories automatically before writing.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If an I/O error occurs when writing to the file.
     */
    public void save(List<Task> tasks) throws IOException {
        if (filePath.getParent() != null && !Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.toFileFormat());
        }

        Files.write(filePath, lines);
    }

    /**
     * Loads the saved tasks from the storage file on the hard disk.
     * If the storage file does not exist at startup, returns an empty list cleanly without errors.
     * If individual lines are corrupted, warnings are displayed and valid tasks are preserved.
     *
     * @return List of valid tasks loaded from disk.
     * @throws IOException If an I/O error occurs when reading the file.
     */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (Files.notExists(filePath)) {
            return tasks;
        }

        List<String> lines = Files.readAllLines(filePath);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (PennyException e) {
                System.out.println("     Warning [Line " + (i + 1) + "]: " + e.getMessage() + " -> Skipped.");
            }
        }
        return tasks;
    }

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line The pipe-delimited line representing a saved task.
     * @return The parsed Task object (Todo, Deadline, or Event).
     * @throws PennyException If the line format is invalid, missing fields, or contains an unknown task type.
     */
    private Task parseTask(String line) throws PennyException {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new PennyException("Corrupted format (insufficient fields)");
        }

        String type = parts[0].trim();
        String statusStr = parts[1].trim();
        String description = parts[2].trim();

        if (!statusStr.equals("0") && !statusStr.equals("1")) {
            throw new PennyException("Invalid status indicator '" + statusStr + "' (expected 0 or 1)");
        }

        if (description.isEmpty()) {
            throw new PennyException("Missing task description");
        }

        boolean isDone = statusStr.equals("1");
        Task task;

        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4 || parts[3].trim().isEmpty()) {
                throw new PennyException("Corrupted deadline task (missing deadline time)");
            }
            task = new Deadline(description, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5 || parts[3].trim().isEmpty() || parts[4].trim().isEmpty()) {
                throw new PennyException("Corrupted event task (missing start or end time)");
            }
            task = new Event(description, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new PennyException("Unknown task type '" + type + "'");
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
