package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasksList = new HashMap<>();
    private LocalDateTime endTime;

    public Epic(String name, String details, Duration duration, LocalDateTime startTime) {
        super(name, details, duration, startTime);
    }

    public Epic(Integer id, String name, TaskStatus status, String details, Duration duration, LocalDateTime startTime) {
        super(id, name, status, details, duration, startTime);
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
        if (subtasksList.size() == newTasks && !subtasksList.isEmpty()) {
            setStatus(TaskStatus.NEW);
        } else if (subtasksList.size() == doneTasks) {
            setStatus(TaskStatus.DONE);
        } else setStatus(TaskStatus.IN_PROGRESS);
    }

    public void deleteSubtasksList(int id) {
        subtasksList.remove(id);
        updateEpicDetails();
    }

    public void addSubtasksList(int id, Subtask subtask) {
        if (this.getId().equals(subtask.getEpicID())) {
            return;
        }
        subtasksList.put(id, subtask);
        updateEpicDetails();
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

    private void updateEpicDetails() {
        duration = Duration.ZERO;
        startTime = null;
        endTime = null;

        for (Subtask subtask : subtasksList.values()) {
            duration = duration.plus(subtask.getDuration());
            if (startTime == null || subtask.getStartTime().isBefore(startTime)) {
                startTime = subtask.getStartTime();
            }
            if (endTime == null || subtask.getEndTime().isAfter(endTime)) {
                endTime = subtask.getEndTime();
            }
        }
        updateStatus();
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
