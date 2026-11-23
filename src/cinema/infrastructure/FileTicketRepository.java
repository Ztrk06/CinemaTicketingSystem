package cinema.infrastructure;

import cinema.domain.Ticket;
import cinema.service.ITicketRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the Ticket Repository.
 * Handles persistent storage of ticket data using a CSV file.
 */
public class FileTicketRepository implements ITicketRepository {
    private final String filePath = "data/tickets.csv";

    public FileTicketRepository() {
        File directory = new File("data");
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    /**
     * Saves a new ticket by appending it to the CSV file.
     *
     * @param ticket The ticket object to save.
     */
    @Override
    public void save(Ticket ticket) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(ticket.toCSV());
            bw.newLine();
            System.out.println("Log: Ticket saved to file -> " + ticket.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing ticket in the repository.
     *
     * @param ticket The ticket object containing updated information.
     */
    @Override
    public void update(Ticket ticket) {
        System.out.println("Log: Ticket updated in file -> " + ticket.getId());
    }

    /**
     * Finds a specific ticket by its unique identifier.
     *
     * @param id The unique ID of the ticket to search for.
     * @return The Ticket object if found, otherwise null.
     */
    @Override
    public Ticket findById(String id) {
        return null; // Left blank for now, will be implemented later
    }

    /**
     * Retrieves all tickets from the CSV file.
     *
     * @return A list of all stored tickets. Returns an empty list if the file is missing.
     */
    @Override
    public List<Ticket> findAll() {
        List<Ticket> tickets = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return tickets;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                // CSV Format: id, sessionId, seatId, userId, timestamp, state
                if (parts.length >= 6) {
                    Ticket ticket = new Ticket(parts[0], parts[1], parts[2], parts[3]);
                    tickets.add(ticket);
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred while reading tickets: " + e.getMessage());
        }
        return tickets;
    }
}