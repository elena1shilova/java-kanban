package tasks;

import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasksList = new HashMap<>();

    public Epic(String name, String details) {
        super(name, details);
    }

    public void updateStatus() {
        int newTasks = 0;
        int doneTasks = 0;
        for (Subtask subtask : subtasksList.values()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                newTasks++;
            } else if (subtask.getStatus() == TaskStatus.DONE)
                doneTasks++;
        }
        if (subtasksList.size() == newTasks) {
            setStatus(TaskStatus.NEW);
        } else if (subtasksList.size() == doneTasks) {
            setStatus(TaskStatus.DONE);
        } else setStatus(TaskStatus.IN_PROGRESS);
    }

    public void deleteSubtasksList(int id) {
        subtasksList.remove(id);
    }

    public void addSubtasksList(int id, Subtask subtask) {
        if (this.getId().equals(subtask.getEpicID())) {
            return;
        }
        subtasksList.put(id, subtask);
    }

    public void clearSubtasksList() {
        subtasksList.clear();
        setStatus(TaskStatus.NEW);
    }

    public HashMap<Integer, Subtask> getSubtasksList() {
        return subtasksList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksList, epic.subtasksList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", status=" + status +
                ", subtasksList=" + subtasksList.keySet() +
                '}';
    }
}
