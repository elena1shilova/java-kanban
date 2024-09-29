package manager;

import java.nio.file.Paths;

public class Managers {

    private static TaskManager taskManager;

    public static TaskManager getDefault() {
        if (taskManager == null) {
            taskManager = new FileBackedTaskManager(Paths.get("test_tasks.csv"));
        }
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
