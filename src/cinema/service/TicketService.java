package cinema.service;

import cinema.domain.Ticket;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing ticket operations.
 * Handles booking confirmations, cancellations, and seat availability checks.
 */
public class TicketService {

    private final ITicketRepository ticketRepository;

    public TicketService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Confirms the booking of a ticket.
     * To comply with the State Pattern flow, it temporarily locks the ticket
     * if it is currently in the AvailableState before confirming.
     *
     * @param ticket The ticket to be confirmed and saved.
     */
    public void confirmBooking(Ticket ticket) {
        if (ticket.getState().getClass().getSimpleName().equals("AvailableState")) {
            ticket.lock();
        }
        ticket.confirm();

        if (ticketRepository.findById(ticket.getId()) == null) {
            ticketRepository.save(ticket);
        } else {
            ticketRepository.update(ticket);
        }
    }

    /**
     * Cancels an existing ticket and updates its status in the repository.
     *
     * @param ticket The ticket to be canceled.
     */
    public void cancelTicket(Ticket ticket) {
        ticket.cancel();
        ticketRepository.update(ticket);
    }

    /**
     * Retrieves all tickets purchased by a specific user.
     *
     * @param userId The unique ID of the user.
     * @return A list of tickets belonging to the specified user.
     */
    public List<Ticket> getTicketsByUser(String userId) {
        List<Ticket> userTickets = new ArrayList<>();
        for (Ticket ticket : ticketRepository.findAll()) {
            if (ticket.getUserId().equals(userId)) {
                userTickets.add(ticket);
            }
        }
        return userTickets;
    }

    /**
     * Checks if a specific seat is available for a given session.
     * A seat is considered unavailable if it is booked or locked (i.e., not canceled).
     *
     * @param sessionId The ID of the session.
     * @param seatId    The ID of the seat (e.g., "A1").
     * @return True if the seat is available, false if it is occupied.
     */
    public boolean isSeatAvailable(String sessionId, String seatId) {
        List<Ticket> allTickets = ticketRepository.findAll();
        for (Ticket t : allTickets) {
            if (t.getSessionId().equals(sessionId) && t.getSeatId().equalsIgnoreCase(seatId)) {
                if (!t.getState().getClass().getSimpleName().equals("CancelledState")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Retrieves the current state name of a specific seat for matrix generation.
     *
     * @param sessionId The ID of the session.
     * @param seatId    The ID of the seat.
     * @return The simple class name of the current state, or "AvailableState" by default.
     */
    public String getSeatStateName(String sessionId, String seatId) {
        List<Ticket> allTickets = ticketRepository.findAll();
        for (Ticket t : allTickets) {
            if (t.getSessionId().equals(sessionId) && t.getSeatId().equalsIgnoreCase(seatId)) {
                String stateName = t.getState().getClass().getSimpleName();
                if (!stateName.equals("CancelledState")) {
                    return stateName;
                }
            }
        }
        return "AvailableState";
    }
}