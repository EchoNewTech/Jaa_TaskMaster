package taskmaster_1;

import java.io.IOException;

public class TaskMaster {
    private final TaskMasterApp taskMasterApp;
    
    public TaskMaster() {
        this.taskMasterApp = new TaskMasterApp();
    }
    
    public static void main(String[] args) {
        TaskMaster taskMaster = new TaskMaster();
        try {
            TaskMasterApp.Menu();
        } catch (IOException e) {
            System.out.println("Wystapil blad w trakcie dzialania aplikacji: " + e.getMessage());
        }
    }
}
