package cinema.domain.state;
import cinema.domain.Ticket;

public class BookedState implements ITicketState {
    @Override
    public void lock(Ticket ticket) {
        System.out.println("Error: A purchased ticket cannot be locked!");
    }
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Error: This ticket has already been paid for!");
    }
    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Ticket canceled. Refund process is being initiated.");
        ticket.setState(new CancelledState());
    }
}