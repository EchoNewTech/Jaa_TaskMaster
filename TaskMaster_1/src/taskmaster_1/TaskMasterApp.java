package taskmaster_1;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TaskMasterApp {
    private static TaskDataBase tasks;
    private static UserDataBase users;
    private static User user;
    private static boolean loggedIn;
    private static Scanner scanner;
    private static int taskId;
    private static int taskIdCounter = 1;

    public TaskMasterApp() {
        users = new UserDataBase();
        tasks = new TaskDataBase();
        scanner = new Scanner(System.in);
    }

    public static void Menu() throws IOException {
        while (true) {
            if (user == null) {
                // Menu dla niezalogowanego użytkownika
                System.out.println("=== Menu Uzytkownika ===");
                System.out.println("1. Rejestracja");
                System.out.println("2. Logowanie");
                System.out.println("3. Zmien haslo");
                System.out.println("4. Usun konto");
                System.out.println("5. Wyjscie z programu");
                System.out.print("Twoj wybor: ");
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1 -> {//Rejestracja nowego użytkownika
                            System.out.println("\n=== Rejestracja ===");
                            System.out.print("Podaj nazwe uzytkownika: ");
                            String newUsername = scanner.nextLine().trim();
                            System.out.print("Podaj haslo: ");
                            String newPassword = scanner.nextLine().trim();
                            //Sprawdzenie danych podanych przy rejestracji
                            if (newUsername.isEmpty() || newUsername.contains(" ") || newUsername.contains("\t")) {
                                System.out.println("Nazwa uzytkownika nie moze byc pusta i zawierac spacji.\n");
                            } else if (newPassword.isEmpty() || newPassword.contains(" ") || newPassword.contains("\t")) {
                                System.out.println("Haslo nie moze byc puste i zawierac spacji.\n");
                            } else if (users.users.containsKey(newUsername)) {
                                System.out.println("Nazwa uzytkownika jest juz zajeta.\n");
                            } else {
                                users.register(newUsername, newPassword);
                                System.out.println("Uzytkownik zarejestrowany pomyslnie.\n");
                            }
                        }
                        case 2 -> { //Logowanie użytkownika
                            System.out.println("\n=== Logowanie ===");
                            if (users.userCount() != 0) {
                                System.out.print("Podaj nazwe uzytkownika: ");
                                String username = scanner.nextLine().trim();
                                System.out.print("Podaj haslo: ");
                                String password = scanner.nextLine().trim();
                                if (users.login(username, password)) {
                                    user = users.users.get(username);
                                    loggedIn = true;
                                    System.out.println("Logowanie udane.\n");
                                } else {
                                    System.out.println("Niepoprawna nazwa uzytkownika lub haslo.\n");
                                }
                            } else {
                                System.out.println("Brak utworzonych kont.\n");
                            }
                        }
                        case 3 -> { //Zmiana hasła użytkownika
                            System.out.println("\n=== Zmiana hasla ===");
                            if (users.userCount() != 0) {
                                System.out.print("Podaj nazwe uzytkownika: ");
                                String username = scanner.nextLine().trim();
                                // Sprawdź, czy nazwa użytkownika nie jest pusta i nie zawiera spacji
                                if (username.isEmpty() || username.contains(" ") || username.contains("\t")) {
                                    System.out.println("Nazwa uzytkownika nie może być pusta i zawierac spacji.\n");
                                    break;
                                }
                                // Sprawdź, czy użytkownik istnieje w bazie danych
                                if (!users.users.containsKey(username)) {
                                    System.out.println("Podana nazwa uzytkownika nie istnieje.\n");
                                    break;
                                }
                                System.out.print("Podaj stare haslo: ");
                                String oldPassword = scanner.nextLine().trim();
                                // Sprawdź, czy hasło nie jest puste i nie zawiera spacji
                                if (oldPassword.isEmpty() || oldPassword.contains(" ") || oldPassword.contains("\t")) {
                                    System.out.println("Haslo nie może byc puste i zawierac spacji.\n");
                                    break;
                                }
                                System.out.print("Podaj nowe haslo: ");
                                String newPasswordChange = scanner.nextLine().trim();
                                // Sprawdź, czy nowe hasło nie jest puste i nie zawiera spacji
                                if (newPasswordChange.isEmpty() || newPasswordChange.contains(" ") || newPasswordChange.contains("\t")) {
                                    System.out.println("Nowe haslo nie moze być puste i zawierac spacji.\n");
                                } else if (user != null && user.changePassword(oldPassword, newPasswordChange)) {
                                    System.out.println("Haslo zostalo zmienione pomyslnie.\n");
                                } else if (newPasswordChange.equals(oldPassword)) {
                                    System.out.println("Nowe haslo nie moze byc takie samo jak stare.\n");
                                } else {
                                    System.out.println("Niepoprawne stare haslo lub nazwa uzytkownika.\n");
                                }
                            } else {
                                System.out.println("Brak utworzonych kont.\n");
                            }
                        }
                        case 4 -> { // Usunięcie konta
                            System.out.println("\n=== Usun konto ===");
                            if (users.userCount() != 0) {
                                System.out.print("Podaj nazwe uzytkownika: ");
                                String usernameToDelete = scanner.nextLine().trim();
                                System.out.print("Podaj haslo: ");
                                String passwordToDelete = scanner.nextLine().trim();
                                if(users.users.containsKey(usernameToDelete)) {
                                    // Sprawdzenie poprawności hasła
                                    User userToDelete = users.users.get(usernameToDelete);
                                    if (userToDelete != null && userToDelete.password.equals(passwordToDelete)) {
                                        System.out.print("Czy na pewno chcesz usunac konto '" + usernameToDelete + "'? (tak/nie): ");
                                        String confirmation = scanner.nextLine().trim();
                                        if (confirmation.equalsIgnoreCase("tak")) {
                                            users.users.remove(usernameToDelete);
                                            System.out.println("Konto '" + usernameToDelete + "' zostalo usuniete.\n");
                                        } else {
                                            System.out.println("Operacja anulowana.\n");
                                        }
                                    } else {
                                        System.out.println("Niepoprawne haslo.\n");
                                    }
                                } else {
                                    System.out.println("Podane konto nie istnieje.\n");
                                }
                            } else {
                                System.out.println("Brak utworzonych kont.\n");
                            }
                        }
                        case 5 -> { //Wyjście z programu
                            System.out.println("\nDziekujemy za korzystanie z TaskMaster. \nWyjscie z programu...");
                            System.exit(0);
                        }
                        default -> {
                            System.out.println("Niepoprawny wybor!\n");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Niepoprawny wybor!\n");
                    scanner.nextLine();
                }
            } else { // Menu dla zalogowanego użytkownika
                System.out.println("=== Menu Zadan ===");
                System.out.println("1. Dodaj zadanie");
                System.out.println("2. Dodaj wydarzenie");
                System.out.println("3. Dodaj zadania/wydarzenia z pliku");
                System.out.println("4. Zmien termin");
                System.out.println("5. Zmien status");
                System.out.println("6. Zmien priorytet");
                System.out.println("7. Wglad do zadan/wydarzen");
                System.out.println("8. Usun zadanie/wydarzenie");
                System.out.println("9. Eksportuj zadania/wydarzenia");
                System.out.println("10. Archiwum");
                System.out.println("11. Wyloguj");
                System.out.print("Twoj wybor: ");
                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine();
                    switch (choice) {
                        case 1 -> { //Dodawanie nowego zadania
                            System.out.println("\n=== Dodawanie zadania ===");
                            System.out.println("Wprowadz opis zadania: ");
                            String description = scanner.nextLine();
                            LocalDate deadline = null;
                            boolean validDate = false;
                            while (!validDate) {
                                try {
                                    System.out.println("Wprowadz termin zadania (RRRR-MM-DD): ");
                                    String deadlineStr = scanner.nextLine();
                                    deadline = LocalDate.parse(deadlineStr);
                                    validDate = true;
                                } catch (DateTimeParseException e) {
                                    System.out.println("Nieprawidlowy format daty! Wprowadz date w formacie: RRRR-MM-DD.");
                                }
                            }
                            int priority = 0;
                            boolean validPriority = false;
                            while (!validPriority) {
                                try {
                                    System.out.println("Wprowadz priorytet zadania (1 - niski, 2 - sredni, 3 - wysoki): ");
                                    priority = scanner.nextInt();
                                    scanner.nextLine();
                                    if (priority >= 1 && priority <= 3) {
                                        validPriority = true;
                                    } else {
                                        System.out.println("Nieprawidlowy priorytet! Podaj wartosc od 1 do 3.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Nieprawidlowa wartosc! Podaj liczbe odpowiadajaca priorytetowi.");
                                    scanner.nextLine();
                                }
                            }
                            PriorityTask newPriorityTask = new PriorityTask(taskIdCounter++, description, deadline, String.valueOf(priority), user);
                            tasks.addTask1(user, newPriorityTask);
                            System.out.println("Zadanie zostalo pomyslnie utworzone.\n");
                        }
                        case 2 -> { // Dodawanie nowego wydarzenia
                            System.out.println("\n=== Dodawanie wydarzenia ===");
                            System.out.println("Wprowadz opis wydarzenia: ");
                            String description = scanner.nextLine();
                            LocalDate eventDate = null;
                            boolean validEventDate = false;
                            while (!validEventDate) {
                                try {
                                    System.out.println("Wprowadz date wydarzenia (RRRR-MM-DD): ");
                                    String eventDateStr = scanner.nextLine();
                                    eventDate = LocalDate.parse(eventDateStr);
                                    validEventDate = true;
                                } catch (DateTimeParseException e) {
                                    System.out.println("Nieprawidlowy format daty! Wprowadz date w formacie: RRRR-MM-DD.");
                                }
                            }
                            System.out.println("Wprowadz lokalizacje wydarzenia: ");
                            String location = scanner.nextLine();
                            int priority = 0;
                            boolean validPriority = false;
                            while (!validPriority) {
                                try {
                                    System.out.println("Wprowadz priorytet wydarzenia (1 - niski, 2 - sredni, 3 - wysoki): ");
                                    priority = scanner.nextInt();
                                    scanner.nextLine();
                                    if (priority >= 1 && priority <= 3) {
                                        validPriority = true;
                                    } else {
                                        System.out.println("Nieprawidlowy priorytet! Podaj wartosc od 1 do 3.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Nieprawidlowa wartosc! Podaj liczbe odpowiadajaca priorytetowi.");
                                    scanner.nextLine();
                                }
                            }
                            Event newEvent = new Event(taskId, description, eventDate, location, String.valueOf(priority), user);
                            tasks.addTask(newEvent);
                            System.out.println("Wydarzenie zostalo pomyslnie utworzone.\n");
                        }
                        case 3 -> { //Dodawanie zadań z pliku
                            System.out.println("\n=== Importowanie z pliku ===");
                            System.out.println("Prawidlowy zapis w pliku: \nDla zadania: TASK - opis zadanie - termin (RRRR-MM-DD) - status (0 - niewykonane, 1 - wykonane) - priorytet (niski, sredni, wysoki)");
                            System.out.println("Dla wydarzenia: EVENT - opis wydarzenia - data wydarzenia - status (0 - niewykonane, 1 - wykonane) - priorytet (niski, sredni, wysoki) - lokalizacja");
                            System.out.println("Przykladowy zapis: \nTASK - Zadanie - 2024-06-05 - 0 - niski\nEVENT - wydarzenie - 2024-06-10 - 0 - wysoki - Wroclaw");
                            System.out.println("Wprowadz nazwe pliku, aby zaimportowac zadania ('nazwa.txt'): ");
                            String filePathImport = scanner.nextLine();
                            try {
                                taskIdCounter = tasks.importTasksFromFile(user, filePathImport, taskIdCounter);
                                System.out.println("Zadania zostaly pomyslnie zaimportowane!\n");
                            } catch (IOException e) {
                                System.out.println("Wystapil blad podczas importowania zadan z pliku: " + e.getMessage());
                                System.out.println("Operacja zostala przerwana.\n");
                            }
                        }
                        case 4 -> { // Zmiana terminu istniejącego zadania
                            System.out.println("\n=== Zmiana terminu ===");
                            List<Task> userTasksChange = tasks.tasksList(user);
                            if (userTasksChange.isEmpty()) {
                                System.out.println("Brak dostepnych zadan.\n");
                            } else {
                                System.out.println("Wybierz zadanie, aby zmienic termin (podaj numer ID zadania): ");
                                for (Task task : userTasksChange) {
                                    System.out.println("Task ID: " + task.ID + ", Opis: " + task.description + " - Termin: " + task.deadline);
                                }
                                System.out.print("Twoj wybor: ");
                                int taskIdToChange = scanner.nextInt();
                                scanner.nextLine();
                                // Znajdź zadanie o wybranym numerze ID
                                Task taskToChange = null;
                                for (Task task : userTasksChange) {
                                    if (task.ID == taskIdToChange) {
                                        taskToChange = task;
                                        break;
                                    }
                                }
                                if (taskToChange != null) {
                                    boolean validDateChange = false;
                                    while (!validDateChange) {
                                        try {
                                            System.out.println("Wprowadz nowy termin zadania (RRRR-MM-DD):");
                                            String newDeadlineStr = scanner.nextLine();
                                            LocalDate newDeadline = LocalDate.parse(newDeadlineStr);
                                            tasks.changeTaskDeadline1(user, taskToChange, newDeadline);
                                            System.out.println("Zmieniono termin wykonania zadania.\n");
                                            validDateChange = true;
                                        } catch (DateTimeParseException e) {
                                            System.out.println("Nieprawidlowy format daty! Wprowadz date w formacie: RRRR-MM-DD.");
                                        }
                                    }
                                } else {
                                    System.out.println("Nie ma zadania o podanym numerze ID.\n");
                                }
                            }
                        }
                        case 5 -> { // Zmiana statusu na zrealizowany
                            System.out.println("\n=== Zmiana statusu ===");
                            List<Task> userTasksStatus = tasks.tasksList(user);
                            if (!userTasksStatus.isEmpty()) {
                                System.out.println("Wybierz numer zadania do oznaczenia jako wykonane (podaj numer ID zadania): ");
                                for (Task task : userTasksStatus) {
                                    System.out.println("ID: " + task.ID + ", Opis: " + task.description);
                                }
                                System.out.print("Twoj wybor: ");
                                int taskIdStatus = scanner.nextInt();
                                scanner.nextLine();
                                // Znajdź zadanie o wybranym numerze ID
                                Task selectedTask = null;
                                for (Task task : userTasksStatus) {
                                    if (task.ID == taskIdStatus) {
                                        selectedTask = task;
                                        break;
                                    }
                                }
                                if (selectedTask != null) {
                                    tasks.markTaskAsCompleted(selectedTask.ID);
                                    System.out.println("Zadanie zostalo oznaczone jako wykonane.\n");
                                } else {
                                    System.out.println("Nie ma zadania o podanym numerze ID.\n");
                                }
                            } else {
                                System.out.println("Brak dostepnych zadan.\n");
                            }
                        }
                        case 6 -> { // Zmiana priorytetu zadania
                            System.out.println("\n=== Zmiana priorytetu ===");
                            List<Task> userTasksPriority = tasks.tasksList(user);
                            if (!userTasksPriority.isEmpty()) {
                                System.out.println("Wybierz numer zadania lub wydarzenia, któremu chcesz zmienić priorytet (podaj numer ID):");
                                for (Task task : userTasksPriority) {
                                    switch (task) {
                                        case PriorityTask priorityTask -> System.out.println("Task ID: " + priorityTask.ID + ", Opis: " + priorityTask.description + " - Priorytet: " + priorityTask.priorityText());
                                        case Event event -> System.out.println("Event ID: " + event.ID + ", Opis: " + event.description + " - Priorytet: " + event.priorityTask.priorityText());
                                        default -> System.out.println("Task ID: " + task.ID + ", Opis: " + task.description);
                                    }
                                }
                                System.out.print("Twój wybór: ");
                                int taskIdPriority = scanner.nextInt();
                                scanner.nextLine();

                                Task selectedTask = null;
                                for (Task task : userTasksPriority) {
                                    if (task.ID == taskIdPriority) {
                                        selectedTask = task;
                                        break;
                                    }
                                }

                                if (selectedTask != null) {
                                    switch (selectedTask) {
                                        case PriorityTask priorityTask -> {
                                            System.out.println("Wybrano zadanie: " + priorityTask.description);
                                            System.out.println("Aktualny priorytet: " + priorityTask.priorityText());
                                            boolean validPriority = false;
                                            while (!validPriority) {
                                                System.out.println("Podaj nowy priorytet (1 - niski, 2 - sredni, 3 - wysoki): ");
                                                int newPriority = scanner.nextInt();
                                                scanner.nextLine();
                                                if (newPriority >= 1 && newPriority <= 3) {
                                                    priorityTask.changePriority(String.valueOf(newPriority));
                                                    System.out.println("Priorytet zadania zostal zmieniony na: " + priorityTask.priorityText());
                                                    validPriority = true;
                                                } else {
                                                    System.out.println("Nieprawidlowy priorytet! Priorytet musi byc wartoscia od 1 do 3. Sprobuj ponownie.");
                                                }
                                            }
                                        }
                                        case Event event -> {
                                            System.out.println("Wybrano wydarzenie: " + event.description);
                                            System.out.println("Aktualny priorytet: " + event.priorityTask.priorityText());
                                            boolean validPriority = false;
                                            while (!validPriority) {
                                                System.out.println("Podaj nowy priorytet (1 - niski, 2 - sredni, 3 - wysoki): ");
                                                int newPriority = scanner.nextInt();
                                                scanner.nextLine();
                                                if (newPriority >= 1 && newPriority <= 3) {
                                                    event.priorityTask.changePriority(String.valueOf(newPriority));
                                                    System.out.println("Priorytet wydarzenia zostal zmieniony.");
                                                    validPriority = true;
                                                } else {
                                                    System.out.println("Nieprawidlowy priorytet! Priorytet musi byc wartoscia od 1 do 3. Sprobuj ponownie.");
                                                }
                                            }
                                        }
                                        default -> {
                                        }
                                    }
                                } else {
                                    System.out.println("Nie ma zadania ani wydarzenia o podanym numerze ID. Sprobuj ponownie.\n");
                                }
                            } else {
                                System.out.println("Brak dostepnych zadan lub wydarzen.\n");
                            }
                        }
                        case 7-> { // Wgląd do utworzonych zadań
                            System.out.println("\n=== Wglad ===");
                            List<Task> updatedTasks = tasks.tasksList(user);
                            if (updatedTasks.isEmpty()) {
                                System.out.println("Brak dostepnych zadan.\n");
                            } else {
                                System.out.println("Twoje zadania: ");
                                for (Task task : updatedTasks) {
                                    task.displayDetails();
                                    System.out.println();
                                }
                            }
                        }
                        case 8 -> { // Usunięcie istniejącego zadania
                            System.out.println("\n=== Usuwanie ===");
                            List<Task> userTasksDelete = tasks.tasksList(user);
                            if (!userTasksDelete.isEmpty()) {
                                System.out.println("Wybierz numer zadania do usuniecia (podaj numer ID zadania): ");
                                for (Task task : userTasksDelete) {
                                    System.out.println("Task ID: " + task.ID + ", Opis: " + task.description);
                                }
                                System.out.print("Twoj wybor: ");
                                int taskIdDelete = scanner.nextInt();
                                scanner.nextLine();
                                // Znajdź zadanie o wybranym numerze ID
                                Task taskToDelete = null;
                                for (Task task : userTasksDelete) {
                                    if (task.ID == taskIdDelete) {
                                        taskToDelete = task;
                                        break;
                                    }
                                }
                                if (taskToDelete != null) {
                                    tasks.removeTask(user, taskToDelete);
                                    System.out.println("Zadanie zostalo usuniete.\n");
                                } else {
                                    System.out.println("Nie ma zadania o podanym numerze ID.\n");
                                }
                            } else {
                                System.out.println("Brak dostepnych zadan.\n");
                            }
                        }
                        case 9 -> { //Eksportowanie zadań do pliku txt
                            System.out.println("\n=== Eksportowanie ===");
                            System.out.println("Wprowadz nazwe pliku, aby wyeksportowac zadania, ktore sa w toku ('nazwa.txt'): ");
                            String filePathExport = scanner.nextLine();
                            tasks.exportTasksToFile(user, filePathExport);
                            System.out.println("Zadania zostaly pomyslnie wyeksportowane!\n");
                        }     
                        case 10 -> { //Archiwum
                            System.out.println("\n=== Archiwum ===");
                            List<Task> archivedTasks = tasks.archivedTasksList();
                            if (!archivedTasks.isEmpty()) {
                                System.out.println("Archiwizowane zadania:");
                                for (Task task : archivedTasks) {
                                    task.displayDetails();
                                    System.out.println();
                                }
                            } else {
                                System.out.println("Brak archiwizowanych zadan.\n");
                            }
                        }
                        case 11 -> { //Wylogowywanie użytkownika
                            user = null;
                            loggedIn = false;
                            System.out.println("Wylogowano pomyslnie.\n");
                        }
                        default -> {
                            System.out.println("Niepoprawny wybor!\n");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Niepoprawny wybor!\n");
                    scanner.nextLine();
                }
            }
        }
    }
}
