package taskmaster_1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDataBase {
    private final Map<Integer, Task> tasks;
    private int nextTaskId;
    
    public TaskDataBase() {
        tasks = new HashMap<>();
        nextTaskId = 1; 
    }
    
    public void addTask(Task task) {
        int taskId = nextTaskId++;
        tasks.put(taskId, task);
    }
    
    public void addTask1(User user, PriorityTask newPriorityTask) {
        tasks.put(newPriorityTask.ID, newPriorityTask);
    }

    public int importTasksFromFile(User user, String filePathImport, int taskIdCounter) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePathImport))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                if (parts.length < 4) {
                    System.out.println("Nieprawidlowy format linii: " + line);
                    continue;
                }
                String taskType = parts[0];
                String description = parts[1];
                boolean completed;
                try {
                    completed = Integer.parseInt(parts[2]) == 1;
                } catch (NumberFormatException e) {
                    System.out.println("Nieprawidlowy format statusu w linii: " + line);
                    continue;
                }
                switch (taskType) {
                    case "TASK" -> {
                        if (parts.length != 5) {
                            System.out.println("Nieprawidlowy format zadania w linii: " + line);
                            continue;
                        }
                        LocalDate deadline;
                        try {
                            deadline = LocalDate.parse(parts[3]);
                        } catch (DateTimeParseException e) {
                            System.out.println("Nieprawidlowy format daty w linii: " + line);
                            continue;
                        }
                        String priority = parts[4].toLowerCase();
                        if (!priority.equals("niski") && !priority.equals("sredni") && !priority.equals("wysoki")) {
                            System.out.println("Nieprawidlowy format priorytetu w linii: " + line);
                            continue;
                        }
                        String priorityValue = switch (priority) {
                            case "niski" -> "1";
                            case "sredni" -> "2";
                            case "wysoki" -> "3";
                            default -> throw new IllegalStateException("Nieprawidlowy priorytet w linii: " + priority);
                        };

                        PriorityTask task = new PriorityTask(taskIdCounter++, description, deadline, priorityValue, user);
                        task.completed = completed;
                        addTask(task);
                    }
                    case "EVENT" -> {
                        if (parts.length != 6) {
                            System.out.println("Nieprawidlowy format wydarzenia w linii: " + line);
                            continue;
                        }
                        LocalDate eventDate;
                        try {
                            eventDate = LocalDate.parse(parts[3]);
                        } catch (DateTimeParseException e) {
                            System.out.println("Nieprawidlowy format daty wydarzenia w linii: " + line);
                            continue;
                        }
                        String priority = parts[4].toLowerCase();
                        if (!priority.equals("niski") && !priority.equals("sredni") && !priority.equals("wysoki")) {
                            System.out.println("Nieprawidlowy format priorytetu w linii: " + line);
                            continue;
                        }
                        String priorityValue = switch (priority) {
                            case "niski" -> "1";
                            case "sredni" -> "2";
                            case "wysoki" -> "3";
                            default -> throw new IllegalStateException("Nieprawidlowy priorytet w linii: " + priority);
                        };
                        String location = parts[5];
                        Event event = new Event(taskIdCounter++, description, eventDate, location, priorityValue, user);
                        event.completed = completed;
                        addTask(event);
                    }
                    default -> System.out.println("Nieznany typ zadania w linii: " + line);
                }
            }
        }
        return nextTaskId;
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }
    
    public void changeTaskDeadline(int taskId, LocalDate newDeadline) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.deadline = newDeadline;
            if (task instanceof Event event) {
                event.deadline = newDeadline;
            }
        }
    }
    
    public void changeTaskDeadline1(User user, Task taskToChange, LocalDate newDeadline) {
        if (user == null || taskToChange == null) {
            return;
        }
        Task storedTask = tasks.get(taskToChange.ID);
        if (storedTask != null && storedTask.belongsTo(user)) {
            storedTask.deadline = newDeadline;
            if(storedTask instanceof Event event) {
                event.deadline = newDeadline;
            }
        }
    }
    
    public void removeTask(User user, Task taskToDelete) {
        if (user == null || taskToDelete == null) {
            return;
        }
        tasks.remove(taskToDelete.ID);
    }
    
    public void markTaskAsCompleted(int taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            task.completed = true;
        }
    }
    
    public void changeTaskPriority(int taskId, String newPriority) {
        Task task = tasks.get(taskId);
        if (task != null && task instanceof PriorityTask) {
            ((PriorityTask) task).changePriority(newPriority);
        }
    }

    public void exportTasksToFile(User user, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            List<Task> userTasks = tasksList(user);
            for (Task task : userTasks) {
                switch (task) {
                    case PriorityTask priorityTask -> writer.write("TASK" + " -  " + priorityTask.description + " - " + (priorityTask.completed ? 1 : 0) + " - " + priorityTask.deadline + " - " + priorityTask.priorityText());
                    case Event event -> writer.write("EVENT" + " - " + event.description + " - " + (event.completed ? 1 : 0) + " - " + event.deadline + " - " + event.priorityTask.priorityText() + " - " + event.location);
                    default -> writer.write(task.description + " - " + task.deadline + " - " + (task.completed ? 1 : 0));
                }
                writer.newLine();
            }
        }
    }
    
    public void archiveTask(User user, Task taskToArchive) {
        Task task = tasks.get(taskToArchive.ID);
        if (task != null && task.belongsTo(user)) {
            ArchivedTask archivedTask;
            if (task instanceof Event event) {
                archivedTask = new ArchivedTask(task.ID, task.description, task.deadline, user, event.location, event.deadline);
            } else {
                archivedTask = new ArchivedTask(task.ID, task.description, task.deadline, user, null, null);
            }
            tasks.put(taskToArchive.ID, archivedTask);
            task.completed = true;
        }
    }
    
    public List<Task> tasksList(User user) {
        List<Task> userTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (!task.completed && task.belongsTo(user)) {
                userTasks.add(task);
            }
        }
        return userTasks;
    }
    
    public List<Task> archivedTasksList() {
        List<Task> archivedTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.completed) {
                archivedTasks.add(task);
            }
        }
        return archivedTasks;
    }
}
