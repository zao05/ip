package penny.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import penny.task.Task;
import penny.task.Todo;

/**
 * Unit test suite for {@link Ui}.
 * Tests user interface formatting operations for task lists, query results, and feedback messages.
 */
public class UiTest {

    private Ui ui;
    private Task sampleTodo;

    @BeforeEach
    public void setUp() {
        ui = new Ui();
        sampleTodo = new Todo("read book");
    }

    @Test
    public void showTaskList_emptyList_returnsEmptyMessage() {
        String output = ui.showTaskList(Collections.emptyList());
        assertEquals("Your task list is empty.", output);
    }

    @Test
    public void showTaskList_nonEmptyList_returnsFormattedNumberedList() {
        List<Task> tasks = List.of(sampleTodo, new Todo("return book"));
        String output = ui.showTaskList(tasks);
        assertTrue(output.startsWith("Here are the tasks in your list:\n"));
        assertTrue(output.contains("1.[T][ ] read book\n2.[T][ ] return book"));
    }

    @Test
    public void showTasksOnDate_emptyList_returnsEmptyMessage() {
        String output = ui.showTasksOnDate(Collections.emptyList(), "Oct 15 2019");
        assertEquals("No tasks found occurring on Oct 15 2019.", output);
    }

    @Test
    public void showTasksOnDate_nonEmptyList_returnsFormattedNumberedList() {
        List<Task> tasks = List.of(sampleTodo);
        String output = ui.showTasksOnDate(tasks, "Oct 15 2019");
        assertEquals("Here are the tasks occurring on Oct 15 2019:\n1.[T][ ] read book", output);
    }

    @Test
    public void showMatchingTasks_emptyList_returnsEmptyMessage() {
        String output = ui.showMatchingTasks(Collections.emptyList(), "read");
        assertEquals("No matching tasks found for keyword: 'read'.", output);
    }

    @Test
    public void showMatchingTasks_nonEmptyList_returnsFormattedNumberedList() {
        List<Task> tasks = List.of(sampleTodo);
        String output = ui.showMatchingTasks(tasks, "read");
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book", output);
    }

    @Test
    public void showTaskAdded_validTask_returnsConfirmationMessage() {
        String output = ui.showTaskAdded(sampleTodo, 1);
        assertTrue(output.contains("Got it. I've added this task:"));
        assertTrue(output.contains("1 tasks in the list."));
    }

    @Test
    public void showTaskMarked_validTask_returnsConfirmationMessage() {
        sampleTodo.markAsDone();
        String output = ui.showTaskMarked(sampleTodo);
        assertTrue(output.contains("Nice! I've marked this task as done:"));
        assertTrue(output.contains("[X] read book"));
    }

    @Test
    public void showTaskUnmarked_validTask_returnsConfirmationMessage() {
        String output = ui.showTaskUnmarked(sampleTodo);
        assertTrue(output.contains("OK, I've marked this task as not done yet:"));
        assertTrue(output.contains("[ ] read book"));
    }

    @Test
    public void showTaskDeleted_validTask_returnsConfirmationMessage() {
        String output = ui.showTaskDeleted(sampleTodo, 0);
        assertTrue(output.contains("Noted. I've removed this task:"));
        assertTrue(output.contains("0 tasks in the list."));
    }
}
