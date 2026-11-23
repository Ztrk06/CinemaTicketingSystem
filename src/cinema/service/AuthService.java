package cinema.service;

import cinema.domain.Admin;
import cinema.domain.User;
import java.util.List;

/**
 * Service responsible for handling user authentication and registration.
 */
public class AuthService {
    private final IUserRepository userRepository;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user based on their email and password.
     * Also checks for the hardcoded root administrator account.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @return The authenticated User object (or Admin), or null if authentication fails.
     */
    public User login(String email, String password) {
        // Check for root admin (hardcoded account)
        if (email.equals("admin") && password.equals("admin123")) {
            return new Admin("A01", "System Administrator", "admin", "admin123");
        }

        User user = userRepository.findByEmail(email);
        if (user != null && user.getPasswordHash().equals(password)) {
            return user;
        }

        return null;
    }

    /**
     * Registers a new user in the system.
     * Validates if the email is already in use or restricted.
     *
     * @param name     The full name of the user.
     * @param email    The email address (must be unique).
     * @param password The password for the new account.
     * @return True if registration is successful, false if the email is invalid or already taken.
     */
    public boolean register(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null || email.equalsIgnoreCase("admin")) {
            return false;
        }

        List<User> allUsers = userRepository.findAll();
        String newId = String.format("U%03d", allUsers.size() + 1);

        User newUser = new User(newId, name, email, password);
        userRepository.save(newUser);
        return true;
    }
}