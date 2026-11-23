package cinema.domain.state;
import cinema.domain.Ticket;

public class CancelledState implements ITicketState {
    @Override
    public void lock(Ticket ticket) {
        System.out.println("Error: A canceled ticket cannot be locked!");
    }
    @Override
    public void pay(Ticket ticket) {
        System.out.println("Error: Payment cannot be accepted for a canceled ticket!");
    }
    @Override
    public void cancel(Ticket ticket) {
        System.out.println("Error: The ticket has already been canceled.");
    }
}