package cinema.domain.state;

import cinema.domain.Ticket;

public class AvailableState implements ITicketState {

    @Override
    public void lock(Ticket ticket) {
        System.out.println("Seat has been temporarily locked.");
        // Updating the state to "Locked"
        ticket.setState(new LockedState());
    }

    @Override
    public void pay(Ticket ticket) {
        System.out.println("Error: Payment cannot be processed for an unlocked or available seat!");
    }

    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Error: A ticket that is not already reserved cannot be canceled.");
    }
}