package taskmaster_1;

import java.time.LocalDate;
import java.util.List;

public class ArchivedTask extends Task {
    private List<String> tags;
    private final String location;
    private final LocalDate eventDate;
    
    public ArchivedTask(int taskId, String description, LocalDate deadline, User user, String location, LocalDate eventDate) {
        super(description, deadline, user);
        this.location = location;
        this.eventDate = eventDate;
    }

    @Override
    public void displayDetails() {
        displayBaseDetails();
        if (location != null) {
            System.out.println("Lokalizacja: " + location);
        }
    }
}
