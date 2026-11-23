package cinema.presentation;

import cinema.domain.Admin;
import cinema.domain.User;
import cinema.infrastructure.RepositoryFactory;
import cinema.service.*;
import java.util.Scanner;

/**
 * The entry point of the application.
 * Bootstraps dependencies, establishes the initial services,
 * and provides the main login and registration interface.
 */
public class Main {
    public static void main(String[] args) {
        // Dependency Injection Setup
        ITicketRepository ticketRepo = RepositoryFactory.createTicketRepository();
        IMovieRepository movieRepo = RepositoryFactory.createMovieRepository();
        ISessionRepository sessionRepo = RepositoryFactory.createSessionRepository();
        IUserRepository userRepo = RepositoryFactory.createUserRepository();

        TicketService ticketService = new TicketService(ticketRepo);
        MovieService movieService = new MovieService(movieRepo);
        SessionService sessionService = new SessionService(sessionRepo, movieService);
        ICreditCardRepository cardRepo = RepositoryFactory.createCreditCardRepository();
        PaymentService paymentService = new PaymentService(cardRepo);
        AuthService authService = new AuthService(userRepo);

        BookingFacade bookingFacade = new BookingFacade(ticketService, paymentService);

        UserMenu userMenu = new UserMenu(movieService, bookingFacade, ticketService, sessionService, paymentService);
        AdminMenu adminMenu = new AdminMenu(movieService, sessionService);

        Scanner scanner = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("  DOKUZ EYLUL CINEMA TICKET SYSTEM   ");
        System.out.println("=====================================");

        while (true) {
            System.out.println("\n1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine();

            if (choice.equals("3")) {
                System.out.println("System is shutting down. Have a good day!");
                break;
            }
            else if (choice.equals("2")) {
                System.out.println("\n--- REGISTER ---");
                System.out.print("Full Name: ");
                String name = scanner.nextLine();

                System.out.print("Email Address (Username): ");
                String email = scanner.nextLine();

                System.out.print("Set Password: ");
                String password = scanner.nextLine();

                boolean isRegistered = authService.register(name, email, password);

                if (isRegistered) {
                    System.out.println("SUCCESS: Registration complete! You can now log in.");
                } else {
                    System.out.println("ERROR: This email address is already in use or invalid!");
                }
            }
            else if (choice.equals("1")) {
                System.out.println("\n--- LOGIN ---");
                System.out.print("Email: ");
                String email = scanner.nextLine();

                System.out.print("Password: ");
                String password = scanner.nextLine();

                User loggedInUser = authService.login(email, password);

                if (loggedInUser != null) {
                    if (loggedInUser instanceof Admin) {
                        adminMenu.show((Admin) loggedInUser);
                    } else {
                        userMenu.show(loggedInUser);
                    }
                    // Loops back to the main menu after logging out instead of terminating
                } else {
                    System.out.println("\nERROR: Invalid email or password! Please try again.");
                }
            }
            else {
                System.out.println("Invalid choice, please enter 1, 2, or 3.");
            }
        }
        scanner.close();
    }
}