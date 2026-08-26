package penny.task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import penny.common.PennyException;

/**
 * Represents and manages the collection of tasks in the Penny application.
 * Provides operations to add, delete, mark, unmark, retrieve, and filter tasks,
 * ensuring robust index bounds validation.
 */
public class TaskList {

    private final List<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList initialized with an existing list of tasks.
     *
     * @param tasks The initial list of tasks.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to be added.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index The 0-based index of the task to remove.
     * @return The removed Task.
     * @throws PennyException If the index is out of bounds or the list is empty.
     */
    public Task delete(int index) throws PennyException {
        validateIndex(index);
        return this.tasks.remove(index);
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param index The 0-based index of the task.
     * @return The task at the specified index.
     * @throws PennyException If the index is out of bounds or the list is empty.
     */
    public Task get(int index) throws PennyException {
        validateIndex(index);
        return this.tasks.get(index);
    }

    /**
     * Marks the task at the specified index as completed.
     *
     * @param index The 0-based index of the task to mark.
     * @return The task that was marked done.
     * @throws PennyException If the index is out of bounds or the list is empty.
     */
    public Task mark(int index) throws PennyException {
        validateIndex(index);
        Task task = this.tasks.get(index);
        task.markAsDone();
        return task;
    }

    /**
     * Marks the task at the specified index as not yet completed.
     *
     * @param index The 0-based index of the task to unmark.
     * @return The task that was unmarked.
     * @throws PennyException If the index is out of bounds or the list is empty.
     */
    public Task unmark(int index) throws PennyException {
        validateIndex(index);
        Task task = this.tasks.get(index);
        task.markAsUndone();
        return task;
    }

    /**
     * Validates that the provided index is within the valid bounds of the task list.
     *
     * @param index The 0-based index to validate.
     * @throws PennyException If the task list is empty or the index is out of bounds.
     */
    private void validateIndex(int index) throws PennyException {
        if (this.tasks.isEmpty()) {
            throw new PennyException("Your task list is empty. Add some tasks first!");
        }
        if (index < 0 || index >= this.tasks.size()) {
            throw new PennyException("Task number " + (index + 1) + " is out of range. You currently have " + this.tasks.size() + " task(s).");
        }
    }

    /**
     * Returns the total number of tasks currently in the list.
     *
     * @return The count of tasks.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return True if there are no tasks, false otherwise.
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }

    /**
     * Returns an unmodifiable list of all tasks.
     *
     * @return List of all tasks.
     */
    public List<Task> getAllTasks() {
        return Collections.unmodifiableList(this.tasks);
    }

    /**
     * Finds and returns all tasks that occur on the specified date.
     *
     * @param targetDate The date to filter tasks by.
     * @return List of tasks occurring on targetDate.
     */
    public List<Task> findTasksOnDate(LocalDate targetDate) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.isOnDate(targetDate)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}
