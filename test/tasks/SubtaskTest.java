package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubtaskTest {
    @Test
    void testSubtaskCannotSetItselfAsEpic() {
        Subtask subtask = new Subtask("Subtask", "subtask1details", 2, Duration.ofNanos(125412), LocalDateTime.now());
        subtask.setId(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            subtask.setEpicID(1);
        });

        String expectedMessage = "Идентификатор подзадачи epicID не может совпадать с ее собственным идентификатором";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
