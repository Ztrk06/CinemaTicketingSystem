package cinema.domain.state;
import cinema.domain.Ticket;

public class LockedState implements ITicketState {
    @Override
    public void lock(Ticket ticket) {
        System.out.println("Error: This seat is already locked!");
    }
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Payment approved. Ticket purchased successfully.");
        ticket.setState(new BookedState());
    }
    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Transaction canceled. The seat is now available.");
        ticket.setState(new AvailableState());
    }
}