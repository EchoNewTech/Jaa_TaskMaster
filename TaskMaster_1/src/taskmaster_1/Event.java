package taskmaster_1;

import java.time.LocalDate;
import java.util.List;


public class Event extends Task {

    protected String location;
    final PriorityTask priorityTask;

    public Event(int taskId, String description, LocalDate deadline, String location, String priority, User user) {
        super(description, deadline, user);
        this.location = location;
        this.priorityTask = new PriorityTask(taskId, description, deadline, priority, user);
    }

    @Override
    public void changePriority(String newPriority) {
        this.priorityTask.changePriority(newPriority);
    }
        
    @Override
    public void displayDetails() {
        displayBaseDetails();
        if(location != null && priorityTask != null) {
            System.out.println("Lokalizacja: " + location);
            System.out.println("Priorytet: " + priorityTask.priorityText());
        }
    }
}
