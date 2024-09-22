package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EpicTest {
    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Epic 1", "epic1details", Duration.ZERO, LocalDateTime.now());
        epic.setId(1);
    }

    @Test
    public void testEpicCannotAddItselfAsSubtask() {

        Subtask subtask = new Subtask("Subtask 1", "subtask1details", 1, Duration.ofNanos(125412), LocalDateTime.now());
        subtask.setId(1);

        epic.addSubtasksList(1, subtask);
        assertFalse(epic.getSubtasksList().containsKey(1));
    }

    @Test
    public void testEpicIfDeleteSubtask() {

        Subtask subtask = new Subtask("Subtask 1", "subtask1details", 2, Duration.ofNanos(125412), LocalDateTime.now());

        epic.addSubtasksList(5, subtask);

        epic.deleteSubtasksList(5);

        assertFalse(epic.getSubtasksList().containsKey(5));
    }

    @Test
    void testUpdateStatusAllNew() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.NEW, "Details 1", 2, Duration.ofHours(1), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2, "Subtask 2", TaskStatus.NEW, "Details 2", 2, Duration.ofHours(2), LocalDateTime.now());

        epic.addSubtasksList(1, subtask1);
        epic.addSubtasksList(2, subtask2);

        epic.updateStatus();

        assertEquals(TaskStatus.NEW, epic.getStatus());
    }

    @Test
    void testUpdateStatusAllDone() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.DONE, "Details 1", 2, Duration.ofHours(1), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2, "Subtask 2", TaskStatus.DONE, "Details 2", 2, Duration.ofHours(2), LocalDateTime.now());

        epic.addSubtasksList(1, subtask1);
        epic.addSubtasksList(2, subtask2);

        epic.updateStatus();

        assertEquals(TaskStatus.DONE, epic.getStatus());
    }

    @Test
    void testUpdateStatusMixedNewAndDone() {
        Subtask subtask1 = new Subtask(2, "Subtask 1", TaskStatus.NEW, "Details 1", 2, Duration.ofHours(1), LocalDateTime.now());
        Subtask subtask2 = new Subtask(3, "Subtask 2", TaskStatus.DONE, "Details 2", 2, Duration.ofHours(2), LocalDateTime.now());

        epic.addSubtasksList(1, subtask1);
        epic.addSubtasksList(2, subtask2);

        epic.updateStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void testUpdateStatusAllInProgress() {
        Subtask subtask1 = new Subtask(1, "Subtask 1", TaskStatus.IN_PROGRESS, "Details 1", 2, Duration.ofHours(1), LocalDateTime.now());
        Subtask subtask2 = new Subtask(2, "Subtask 2", TaskStatus.IN_PROGRESS, "Details 2", 2, Duration.ofHours(2), LocalDateTime.now());

        epic.addSubtasksList(1, subtask1);
        epic.addSubtasksList(2, subtask2);

        epic.updateStatus();

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus());
    }
}
