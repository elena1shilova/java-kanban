package manager;

import exception.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;
import tasks.TaskType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    private static final String HEADER = "id,type,name,status,description,duration,startTime,epic\n";

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(filePath)))) {
            writer.write(HEADER);
            for (Task task : getTasksList()) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : getEpicsList()) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : getSubtasksList()) {
                writer.write(toString(subtask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%d,%s",
                task.getId(),
                TaskType.TASK,
                task.getName(),
                task.getStatus(),
                task.getDetails(),
                task.getDuration().toMinutes(),
                task.getStartTime() != null ? task.getStartTime().toString() : "");
    }

    private String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%d,%s",
                epic.getId(),
                TaskType.EPIC,
                epic.getName(),
                epic.getStatus(),
                epic.getDetails(),
                epic.getDuration().toMinutes(),
                epic.getStartTime() != null ? epic.getStartTime().toString() : "");
    }

    private String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s",
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getName(),
                subtask.getStatus(),
                subtask.getDetails(),
                subtask.getDuration().toMinutes(),
                subtask.getStartTime() != null ? subtask.getStartTime().toString() : "",
                subtask.getEpicID());
    }

    @Override
    public Integer addNewTask(Task task) {

        boolean hasIntersection = getPrioritizedTasks().stream()
                .anyMatch(existingTask -> isIntersect(existingTask, task));
        if (hasIntersection) {
            throw new RuntimeException();
        }
        super.addNewTask(task);
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        save();
        return task.getId();
    }

    public boolean isIntersect(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null) {
            return false;
        }

        LocalDateTime endTime1 = task1.getEndTime();
        LocalDateTime endTime2 = task2.getEndTime();

        return task1.getStartTime().isBefore(endTime2) && endTime1.isAfter(task2.getStartTime());
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        }
        save();
        return epic.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        save();
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        } else {
            prioritizedTasks.remove(task);
        }
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        } else {
            prioritizedTasks.remove(subtask);
        }
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic.getStartTime() != null) {
            prioritizedTasks.add(epic);
        } else {
            prioritizedTasks.remove(epic);
        }
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        Task task = tasks.remove(id);
        if (task != null && task.getStartTime() != null) {
            prioritizedTasks.remove(task);
        }
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null && subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
        }
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.remove(id);
        if (epic != null && epic.getStartTime() != null) {
            prioritizedTasks.remove(epic);
        }
        super.deleteEpic(id);
        save();
    }

    private static Task fromString(String value) {
        String[] string = value.split(",");

        Duration duration = Duration.ofMinutes(Long.parseLong(string[5]));
        LocalDateTime startTime = string[6].isEmpty() ? null : LocalDateTime.parse(string[6]);

        return switch (TaskType.valueOf(string[1])) {
            case TASK ->
                    new Task(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], duration, startTime);
            case EPIC ->
                    new Epic(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], duration, startTime);
            case SUBTASK ->
                    new Subtask(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[7]), duration, startTime);
        };
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        int maxId = 0;
        try (BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                int taskId = task.getId();
                if (taskId > maxId) {
                    maxId = taskId;
                }
                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof Subtask) {
                    Subtask subtask = (Subtask) task;
                    manager.subtasks.put(task.getId(), subtask);

                    Epic epic = manager.epics.get(subtask.getEpicID());
                    if (epic != null) {
                        epic.addSubtasksList(task.getId(), subtask);
                    }
                } else {
                    manager.tasks.put(task.getId(), task);
                }
            }
            manager.taskId = maxId;
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

}
