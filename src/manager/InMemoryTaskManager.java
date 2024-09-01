package manager;

import exeption.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private Integer taskId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public Task getTask(Integer taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(Integer taskId) {
        Subtask subtask = subtasks.get(taskId);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(Integer taskId) {
        Epic epic = epics.get(taskId);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void addNewTask(Task task) throws ManagerSaveException {
        task.setId(taskId);
        tasks.put(taskId, task);
        taskId++;
    }

    @Override
    public void addNewEpic(Epic epic) throws ManagerSaveException {
        epic.setId(taskId);
        epics.put(taskId, epic);
        taskId++;
    }

    @Override
    public void addNewSubtask(Subtask subtask) throws ManagerSaveException {
        if (epics.containsKey(subtask.getEpicID())) {
            subtask.setId(taskId);
            subtasks.put(taskId, subtask);
            epics.get(subtask.getEpicID()).addSubtasksList(taskId, subtask);
            epics.get(subtask.getEpicID()).updateStatus();
            taskId++;
        }
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        Integer id = subtask.getId();
        if (subtasks.containsKey(id)) {
            subtasks.put(id, subtask);
            Epic epic = epics.get(subtask.getEpicID());
            if (epic != null) {
                epic.addSubtasksList(id, subtask);
                epic.updateStatus();
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        Integer id = epic.getId();
        if (epics.containsKey(id)) {
            Epic existingEpic = epics.get(id);
            existingEpic.setName(epic.getName());
            existingEpic.setDetails(epic.getDetails());
            epics.put(id, existingEpic);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Task t : tasks.values()) {
            historyManager.remove(t.getId());
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        for (Epic epic : epics.values()) {
            epic.clearSubtasksList();
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        for (Epic epic : epics.values()) {
            historyManager.remove(epic.getId());
            epic.clearSubtasksList();
        }
        subtasks.clear();
        epics.clear();

    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        if (subtasks.get(id) != null) {
            Subtask subtask = subtasks.get(id);
            Integer epicID = subtask.getEpicID();
            Epic epic = epics.get(epicID);
            if (epic != null) {
                epic.deleteSubtasksList(id);
                epic.updateStatus();
            }
            subtasks.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(Integer id) {
        if (epics.get(id) != null) {
            for (Integer subtaskID : epics.get(id).getSubtasksList().keySet()) {
                subtasks.get(subtaskID).setEpicID(null);
                epics.get(id).deleteSubtasksList(subtaskID);
                historyManager.remove(subtaskID);
            }
            epics.remove(id);
            historyManager.remove(id);
        }
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(Integer epicID) {
        List<Subtask> subtaskList = new ArrayList<>();
        if (epics.containsKey(epicID)) {
            Epic epic = epics.get(epicID);
            for (Integer subtaskId : epic.getSubtasksList().keySet()) {
                subtaskList.add(subtasks.get(subtaskId));
            }
        }
        return subtaskList;
    }
}
