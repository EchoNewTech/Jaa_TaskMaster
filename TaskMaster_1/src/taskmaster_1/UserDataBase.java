package taskmaster_1;

import java.util.HashMap;
import java.util.Map;

public class UserDataBase {
    public Map<String, User> users;

    public UserDataBase() {
        users = new HashMap<>();
    }

    public void register(String username, String password) {
        users.put(username, new User(username, password));
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            return false; 
        }
        return password.equals(user.password);
    }

    public int userCount() {
        return users.size();
    }
}
