import org.junit.jupiter.api.Test;
import tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void testSubtaskCannotSetItselfAsEpic() {
        Subtask subtask = new Subtask("Subtask", "subtask1details", 2);
        subtask.setId(1);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            subtask.setEpicID(1);
        });

        String expectedMessage = "Идентификатор подзадачи epicID не может совпадать с ее собственным идентификатором";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
