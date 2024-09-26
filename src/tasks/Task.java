package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    protected String name;
    protected String details;
    protected Integer id;
    protected TaskStatus status = TaskStatus.NEW;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String details, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.details = details;
        this.duration = duration;
        this.startTime = startTime;

    }

    public Task(Integer id, String name, TaskStatus status, String details) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.status = status;
    }

    public Task(Integer id, String name, TaskStatus status, String details, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.details = details;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id == null) {
            this.id = id;
        }
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) && Objects.equals(details, task.details) && Objects.equals(id, task.id) && status == task.status && Objects.equals(duration, task.duration) && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, details, id, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", status=" + status +
                '}';
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}

