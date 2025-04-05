package taskmaster_1;

import java.time.LocalDate;

public abstract class Task {
    protected static int nextID = 1;
    protected int ID;
    protected String description;
    protected LocalDate deadline;
    protected boolean completed;
    protected String priority;
    protected User user;
    
    public Task(String description, LocalDate deadline, User user) {
        this.ID = nextID++; 
        this.description = description;
        this.deadline = deadline;
        this.completed = false;
        this.priority = "brak";
        this.user = user;
    }

    public abstract void displayDetails();
    
    protected void displayBaseDetails() {
        System.out.println("ID: " + ID);
        System.out.println("Opis: " + description);
        System.out.println("Termin: " + deadline);
        System.out.println("Status:  " + (completed ? "Wykonane" : "Niewykonane"));
    }
        
    public void changePriority(String priority) {
        this.priority = priority != null ? priority : "brak";
    }


    public boolean belongsTo(User user) {
        return this.user.equals(user);
    }
}
