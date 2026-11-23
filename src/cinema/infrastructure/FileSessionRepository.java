package cinema.infrastructure;

import cinema.domain.Session;
import cinema.service.ISessionRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the Session Repository.
 * Handles persistent storage of movie session data using a CSV file.
 */
public class FileSessionRepository implements ISessionRepository {
    private final String filePath = "data/sessions.csv";

    public FileSessionRepository() {
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
     * Saves a new session by appending it to the CSV file.
     *
     * @param session The session object to save.
     */
    @Override
    public void save(Session session) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(session.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all sessions from the CSV file.
     *
     * @return A list of all stored sessions. Returns an empty list if the file is empty or missing.
     */
    @Override
    public List<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Session session = new Session(
                            parts[0],
                            parts[1],
                            parts[2],
                            Integer.parseInt(parts[3]), // startHour
                            Double.parseDouble(parts[4])
                    );
                    sessions.add(session);
                }
            }
        } catch (IOException e) {
            // Returns an empty list if the file is empty or does not exist
        }
        return sessions;
    }

    /**
     * Finds a specific session by its unique identifier.
     *
     * @param id The unique ID of the session to search for.
     * @return The Session object if found, otherwise null.
     */
    @Override
    public Session findById(String id) {
        for (Session s : findAll()) {
            if (s.getId().equals(id)) return s;
        }
        return null;
    }
}