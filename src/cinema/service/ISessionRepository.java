package cinema.service;

import cinema.domain.Session;
import java.util.List;

public interface ISessionRepository {
    void save(Session session);
    List<Session> findAll();
    Session findById(String id);
}