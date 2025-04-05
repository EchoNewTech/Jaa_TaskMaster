package taskmaster_1;

import java.time.LocalDate;

public class PriorityTask extends Task {
    protected String priorityValue;

    public PriorityTask(int taskId, String description, LocalDate deadline, String priority, User user) {
        super(description, deadline, user);
        this.priorityValue = priority;
    }
    
    public String priorityText() {
        return switch (priorityValue) {
            case "1" -> "niski";
            case "2" -> "sredni";
            case "3" -> "wysoki";
            default -> "brak";
        };
    }
    
    @Override
    public void displayDetails() {
        displayBaseDetails();
        System.out.println("Priorytet: " + priorityText());
    }

}
