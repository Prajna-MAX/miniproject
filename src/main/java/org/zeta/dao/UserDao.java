package org.zeta.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeta.model.User;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String FILE_NAME = "users.json";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static List<User> users = new ArrayList<>();


    static {
        loadFromFile();
    }

    private static void loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists() && file.length() > 0) {
                users = mapper.readValue(file, new TypeReference<List<User>>() {});
            } else {
                users = new ArrayList<>();
                mapper.writeValue(file, users);
            }
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            users = new ArrayList<>();
        }
    }
    private static void saveToFile() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), users);
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static void addUser(User user) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                throw new RuntimeException("User already exists");
            }
        }
        users.add(user);
        saveToFile();
    }


    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static boolean userExists(String username) {
        return getUser(username) != null;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
}
