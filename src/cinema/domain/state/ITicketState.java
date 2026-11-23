package cinema.domain.state;

import cinema.domain.Ticket;

public interface ITicketState {
    void lock(Ticket ticket);
    void pay(Ticket ticket);
    void cancel(Ticket ticket);
}