package cinema.service;

import cinema.domain.Ticket;
import java.util.List;

public interface ITicketRepository {
    void save(Ticket ticket);
    void update(Ticket ticket);
    Ticket findById(String id);
    List<Ticket> findAll();
}