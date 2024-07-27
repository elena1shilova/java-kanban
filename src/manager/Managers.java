package manager;

public class Managers {
    private TaskManager taskManager;
    private HistoryManager historyManager;

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
