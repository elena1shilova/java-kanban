import manager.ITaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        ITaskManager taskManager = new TaskManager();

        /*
        Создание новой задачи
         */
        Task task1 = new Task("Task 1", "task1details");
        Integer taskId1 = taskManager.addNewTask(task1);
        System.out.println("Task ID: " + taskId1);
        System.out.println(taskManager.getTask(taskId1));
        Task task2 = new Task("Task 2", "task2details");
        Integer taskId2 = taskManager.addNewTask(task2);
        System.out.println("Task ID: " + taskId2);
        System.out.println(taskManager.getTask(taskId2));

        /*
        Создание нового эпика
         */
        Epic epic = new Epic("Epic 1", "epic1details");
        Integer epicId = taskManager.addNewEpic(epic);
        System.out.println("Epic ID: " + epicId);
        System.out.println(taskManager.getEpic(epicId));

        /*
        Создание новой подзадачи
         */
        Subtask subtask = new Subtask("Subtask 1", "subtask1details", epicId);
        Integer subtaskId = taskManager.addNewSubtask(subtask);
        System.out.println("Subtask ID: " + subtaskId);
        System.out.println(taskManager.getSubtask(subtaskId));

        /*
        Обновление задачи
         */
        task1.setName("Обновление Task 1");
        taskManager.updateTask(task1);
        System.out.println(taskManager.getTask(taskId1));

        /*
        Обновление подзадачи
         */
        subtask.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtask, subtaskId);
        System.out.println(taskManager.getSubtask(subtaskId));

        /*
        Обновление эпика
         */
        epic.setName("Обновление Epic 1");
        taskManager.updateEpic(epic, epicId);
        System.out.println(taskManager.getEpic(epicId));

        /*
        Получение списка всех задач
         */
        System.out.println("Все tasks: " + taskManager.getTasksList());

        /*
        Получение списка всех подзадач
         */
        System.out.println("Все subtasks: " + taskManager.getSubtasksList());

        /*
        Получение списка всех эпиков
         */
        System.out.println("Все epics: " + taskManager.getEpicsList());

        /*
        Удаление задачи
         */
        taskManager.deleteTask(taskId1);
        System.out.println("Task после удаления: " + taskManager.getTask(taskId1));
        /*
        проверяем что остался второй task
         */
        System.out.println("Оставшиеся tasks: " + taskManager.getTasksList());

        /*
        Удаление подзадачи
         */
        taskManager.deleteSubtask(subtaskId);
        System.out.println("Subtask после удаления: " + taskManager.getSubtask(subtaskId));

        /*
        Удаление эпика
         */
        taskManager.deleteEpic(epicId);
        System.out.println("Epic после удаления: " + taskManager.getEpic(epicId));

        /*
        Удаление всех задач
         */
        taskManager.deleteAllTasks();
        System.out.println("Все tasks после удаления: " + taskManager.getTasksList());

        /*
        Удаление всех подзадач
         */
        taskManager.deleteAllSubtasks();
        System.out.println("Все subtasks после удаления: " + taskManager.getSubtasksList());

        /*
        Удаление всех эпиков
        taskManager.deleteAllEpics();
         */
        System.out.println("Все epics после удаления: " + taskManager.getEpicsList());

    }
}
