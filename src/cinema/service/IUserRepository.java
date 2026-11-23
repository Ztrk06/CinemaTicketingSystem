package cinema.service;

import cinema.domain.User;
import java.util.List;

public interface IUserRepository {
    void save(User user);
    List<User> findAll();
    User findByEmail(String email);
}