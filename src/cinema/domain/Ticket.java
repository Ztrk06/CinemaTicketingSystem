package cinema.domain;

import cinema.domain.state.AvailableState;
import cinema.domain.state.BookedState;
import cinema.domain.state.ITicketState;

public class Ticket {
    private String id;
    private String sessionId;
    private String seatId;
    private String userId;
    private long timestamp;
    private ITicketState state;

    public Ticket(String id, String sessionId, String seatId, String userId) {
        this.id = id;
        this.sessionId = sessionId;
        this.seatId = seatId;
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.state = new AvailableState();
    }

    // State pattern delegation methods
    // The ticket completely delegates behavior to sub-state classes based on its current state (Locked, Booked, etc.)
    public void lock() {
        state.lock(this);
    }

    public void pay() {
        state.pay(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public String getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSeatId() {
        return seatId;
    }

    public String getUserId() {
        return userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public ITicketState getState() {
        return state;
    }

    public void setState(ITicketState state) {
        this.state = state;
    }

    public String toCSV() {
        return id + "," + sessionId + "," + seatId + "," + userId + "," + timestamp + "," + state.getClass().getSimpleName();
    }

    // Method that transitions the ticket to the Booked state
    public void confirm() {
        // If there is a method to set states directly in your project:
        this.setState(new BookedState());

        // OR if you are using delegation through the interface:
        // this.state.confirm(this);
    }
}