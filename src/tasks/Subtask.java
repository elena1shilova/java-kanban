package tasks;

import java.util.Objects;

public class Subtask extends Task {

    private Integer epicID;

    public Subtask(String name, String details, Integer epicID) {
        super(name, details);
        if (epicID.equals(this.getId())) {
            throw new IllegalArgumentException("Идентификатор подзадачи epicID не может совпадать с ее собственным идентификатором");
        }
        this.epicID = epicID;
    }

    public Subtask(Integer id, String name, TaskStatus status, String details, Integer epicID) {
        super(id, name, status, details);
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

