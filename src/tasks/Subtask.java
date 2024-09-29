package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {

    private Integer epicID;

    public Subtask(String name, String details, Integer epicID, Duration duration, LocalDateTime startTime) {
        super(name, details, duration, startTime);
        if (epicID.equals(this.getId())) {
            throw new IllegalArgumentException("Идентификатор подзадачи epicID не может совпадать с ее собственным идентификатором");
        }
        this.epicID = epicID;
    }

    public Subtask(Integer id, String name, TaskStatus status, String details, Integer epicID, Duration duration, LocalDateTime startTime) {
        super(id, name, status, details, duration, startTime);
        this.epicID = epicID;
    }

    public Subtask(String name, TaskStatus status, String details, Integer epicID, Duration duration, LocalDateTime startTime) {
        super(name, details, duration, startTime);
        this.status = status;
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(epicID, subtask.epicID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }

    public void setEpicID(Integer epicID) {
        if (epicID.equals(this.getId())) {
            throw new IllegalArgumentException("Идентификатор подзадачи epicID не может совпадать с ее собственным идентификатором");
        }
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", status=" + status +
                ", epicID=" + epicID +
                '}';
    }
}

