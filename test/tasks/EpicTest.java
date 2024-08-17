package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class EpicTest {
    @Test
    public void testEpicCannotAddItselfAsSubtask() {
        Epic epic = new Epic("Epic 1", "epic1details");
        epic.setId(1);

        Subtask subtask = new Subtask("Subtask 1", "subtask1details", 1);
        subtask.setId(1);

        epic.addSubtasksList(1, subtask);
        assertFalse(epic.getSubtasksList().containsKey(1));
    }

    @Test
    public void testEpicIfDeleteSubtask() {
        Epic epic = new Epic("Epic 1", "epic1details");
        epic.setId(1);

        Subtask subtask = new Subtask("Subtask 1", "subtask1details", 2);

        epic.addSubtasksList(5, subtask);

        epic.deleteSubtasksList(5);

        assertFalse(epic.getSubtasksList().containsKey(5));
    }
}
