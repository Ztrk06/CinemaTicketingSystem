package cinema.infrastructure;

import cinema.service.ICreditCardRepository;
import cinema.service.IMovieRepository;
import cinema.service.ISessionRepository;
import cinema.service.ITicketRepository;
import cinema.service.IUserRepository;

/**
 * Factory class for creating repository instances.
 * Implements the Factory Method creational design pattern to facilitate
 * manual Dependency Injection. This prevents services from being tightly
 * coupled to concrete file-based implementations.
 */
public class RepositoryFactory {

    /**
     * Creates an instance of ITicketRepository.
     *
     * @return A concrete implementation of ITicketRepository.
     */
    public static ITicketRepository createTicketRepository() {
        return new FileTicketRepository();
    }

    /**
     * Creates an instance of IMovieRepository.
     *
     * @return A concrete implementation of IMovieRepository.
     */
    public static IMovieRepository createMovieRepository() {
        return new FileMovieRepository();
    }

    /**
     * Creates an instance of ISessionRepository.
     *
     * @return A concrete implementation of ISessionRepository.
     */
    public static ISessionRepository createSessionRepository() {
        return new FileSessionRepository();
    }

    /**
     * Creates an instance of IUserRepository.
     *
     * @return A concrete implementation of IUserRepository.
     */
    public static IUserRepository createUserRepository() {
        return new FileUserRepository();
    }

    /**
     * Creates an instance of ICreditCardRepository.
     *
     * @return A concrete implementation of ICreditCardRepository.
     */
    public static ICreditCardRepository createCreditCardRepository() {
        return new FileCreditCardRepository();
    }
}