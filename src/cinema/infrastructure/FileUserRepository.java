package cinema.infrastructure;

import cinema.domain.User;
import cinema.service.IUserRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the User Repository.
 * Handles persistent storage of user data using a CSV file.
 */
public class FileUserRepository implements IUserRepository {
    private final String filePath = "data/users.csv";

    public FileUserRepository() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves a new user by appending it to the CSV file.
     *
     * @param user The user object to save.
     */
    @Override
    public void save(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(user.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all users from the CSV file.
     *
     * @return A list of all stored users. Returns an empty list if the file is missing or unreadable.
     */
    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            // Intentionally left empty. Returns an empty list if an exception occurs.
        }
        return users;
    }

    /**
     * Finds a specific user by their email address.
     *
     * @param email The email address of the user to search for.
     * @return The User object if found, otherwise null.
     */
    @Override
    public User findByEmail(String email) {
        for (User u : findAll()) {
            if (u.getEmail().equalsIgnoreCase(email)) return u;
        }
        return null;
    }
}