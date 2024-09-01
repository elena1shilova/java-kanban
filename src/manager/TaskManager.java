package manager;

import exeption.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    /*
    методы запроса инф-ии по ид
     */
    Task getTask(Integer taskID);

    Subtask getSubtask(Integer taskID);

    Epic getEpic(Integer taskID);

    /*
    методы запроса всей инф-ии в виде списка
     */
    ArrayList<Task> getTasksList();

    ArrayList<Subtask> getSubtasksList();

    ArrayList<Epic> getEpicsList();

    /*
    создание новой таски/эпика/сабтаски
     */
    void addNewTask(Task task) throws ManagerSaveException;

    void addNewEpic(Epic epic) throws ManagerSaveException;

    void addNewSubtask(Subtask subtask) throws ManagerSaveException;

    /*
    обновление таски/эпика/сабтаски
     */
    void updateTask(Task task) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    /*
    удаление таски/эпика/сабтаски по ид (или всех)
     */
    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();


    void deleteTask(Integer id);

    void deleteSubtask(Integer id);

    void deleteEpic(Integer id);

    List<Subtask> getSubtasksOfEpic(Integer epicID);

    List<Task> getHistory();
}
