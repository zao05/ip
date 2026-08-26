package penny.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import penny.common.PennyException;

/**
 * Unit test suite for {@link TaskList}.
 * Tests task operations including delete, mark, unmark, date-based filtering, and keyword search.
 */
public class TaskListTest {

    private TaskList taskList;
    private Todo sampleTodo;
    private Deadline sampleDeadline;

    @BeforeEach
    public void setUp() throws PennyException {
        taskList = new TaskList();
        sampleTodo = new Todo("read book");
        sampleDeadline = new Deadline("submit assignment", "2019-10-15");
        taskList.add(sampleTodo);
        taskList.add(sampleDeadline);
    }

    @Test
    public void delete_validIndex_taskRemovedSuccessfully() throws PennyException {
        assertEquals(2, taskList.size());
        Task deletedTask = taskList.delete(0);
        assertEquals(sampleTodo, deletedTask);
        assertEquals(1, taskList.size());
        assertEquals(sampleDeadline, taskList.get(0));
    }

    @Test
    public void delete_emptyList_exceptionThrown() {
        TaskList emptyList = new TaskList();
        PennyException exception = assertThrows(PennyException.class, () -> {
            emptyList.delete(0);
        });
        assertTrue(exception.getMessage().contains("Your task list is empty"));
    }

    @Test
    public void delete_negativeIndex_exceptionThrown() {
        PennyException exception = assertThrows(PennyException.class, () -> {
            taskList.delete(-1);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void delete_indexOutOfBounds_exceptionThrown() {
        PennyException exception = assertThrows(PennyException.class, () -> {
            taskList.delete(5);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void mark_validIndex_taskMarkedDone() throws PennyException {
        assertFalse(sampleTodo.isDone());
        Task markedTask = taskList.mark(0);
        assertTrue(markedTask.isDone());
        assertEquals("X", markedTask.getStatusIcon());
    }

    @Test
    public void unmark_validIndex_taskMarkedUndone() throws PennyException {
        taskList.mark(0);
        assertTrue(sampleTodo.isDone());
        Task unmarkedTask = taskList.unmark(0);
        assertFalse(unmarkedTask.isDone());
        assertEquals(" ", unmarkedTask.getStatusIcon());
    }

    @Test
    public void findTasksOnDate_matchingDate_returnsMatchingTasks() {
        LocalDate targetDate = LocalDate.of(2019, 10, 15);
        List<Task> matchingTasks = taskList.findTasksOnDate(targetDate);
        assertEquals(1, matchingTasks.size());
        assertEquals(sampleDeadline, matchingTasks.get(0));
    }

    @Test
    public void findTasksOnDate_nonMatchingDate_returnsEmptyList() {
        LocalDate nonMatchingDate = LocalDate.of(2025, 1, 1);
        List<Task> matchingTasks = taskList.findTasksOnDate(nonMatchingDate);
        assertTrue(matchingTasks.isEmpty());
    }

    @Test
    public void findTasksByKeyword_matchingKeyword_returnsMatchingTasks() {
        List<Task> matchingTasks = taskList.findTasksByKeyword("book");
        assertEquals(1, matchingTasks.size());
        assertEquals(sampleTodo, matchingTasks.get(0));
    }

    @Test
    public void findTasksByKeyword_caseInsensitive_returnsMatchingTasks() {
        List<Task> matchingTasks = taskList.findTasksByKeyword("BOOK");
        assertEquals(1, matchingTasks.size());
        assertEquals(sampleTodo, matchingTasks.get(0));
    }

    @Test
    public void findTasksByKeyword_nonMatchingKeyword_returnsEmptyList() {
        List<Task> matchingTasks = taskList.findTasksByKeyword("nonexistent");
        assertTrue(matchingTasks.isEmpty());
    }
}
