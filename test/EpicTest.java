import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

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
}
