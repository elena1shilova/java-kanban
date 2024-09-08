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

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    private static final String HEDER = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(Path filePath) {
        this.filePath = filePath;
    }


    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(filePath)))) {
            writer.write(HEDER);
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

    String toString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%s",
                task.getId(),
                TaskType.TASK,
                task.getName(),
                task.getStatus(),
                task.getDetails(),
                "");
    }

    String toString(Epic epic) {
        return String.format("%d,%s,%s,%s,%s,%s",
                epic.getId(),
                TaskType.EPIC,
                epic.getName(),
                epic.getStatus(),
                epic.getDetails(),
                "");
    }

    String toString(Subtask subtask) {
        return String.format("%d,%s,%s,%s,%s,%d",
                subtask.getId(),
                TaskType.SUBTASK,
                subtask.getName(),
                subtask.getStatus(),
                subtask.getDetails(),
                subtask.getEpicID());
    }

    @Override
    public Integer addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public Integer addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public Integer addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    public static Task fromString(String value) {
        String[] string = value.split(",");
        return switch (TaskType.valueOf(string[1])) {
            case TASK -> new Task(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4]);
            case EPIC -> new Epic(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4]);
            case SUBTASK ->
                    new Subtask(Integer.parseInt(string[0]), string[2], TaskStatus.valueOf(string[3]), string[4], Integer.parseInt(string[5]));
        };
    }

    public static FileBackedTaskManager loadFromFile(Path file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader reader = Files.newBufferedReader(file)) {

            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    manager.addNewEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    manager.addNewSubtask((Subtask) task);
                } else {
                    manager.addNewTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
        return manager;
    }

}
