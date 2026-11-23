package cinema.presentation;

import cinema.domain.*;
import cinema.service.*;
import cinema.service.strategy.StandardPricing;
import cinema.service.strategy.StudentPricing;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the console-based User Interface for standard customers.
 * Provides functionalities for browsing movies, viewing sessions,
 * selecting seats, managing tickets, and processing payments.
 */
public class UserMenu {
    private final MovieService movieService;
    private final BookingFacade bookingFacade;
    private final TicketService ticketService;
    private final SessionService sessionService;
    private final PaymentService paymentService;

    public UserMenu(MovieService movieService, BookingFacade bookingFacade, TicketService ticketService, SessionService sessionService, PaymentService paymentService) {
        this.movieService = movieService;
        this.bookingFacade = bookingFacade;
        this.ticketService = ticketService;
        this.sessionService = sessionService;
        this.paymentService = paymentService;
    }

    /**
     * Displays the interactive user panel menu.
     *
     * @param user The user currently logged in.
     */
    public void show(User user) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=====================================");
            System.out.println("===  WELCOME TO THE CINEMA SYSTEM ===");
            System.out.println("=====================================");
            System.out.println("Active User: " + user.getName() + " (" + user.getEmail() + ")");
            System.out.println("\n1. List All Movies");
            System.out.println("2. Filter by Genre");
            System.out.println("3. Search by Movie Title");
            System.out.println("4. My Tickets (View and Refund)");
            System.out.println("5. Logout");
            System.out.print("\nPlease select an operation: ");

            int mainChoice = scanner.nextInt();
            scanner.nextLine();

            // 1. LOGOUT
            if (mainChoice == 5) {
                System.out.println("Logging out. Have a nice day!");
                break;
            }
            // 2. VIEW TICKETS AND REFUND/CANCEL SCREEN
            else if (mainChoice == 4) {
                List<Ticket> myTickets = ticketService.getTicketsByUser(user.getId());
                List<Ticket> activeTickets = new ArrayList<>();

                for (Ticket t : myTickets) {
                    if (!t.getState().getClass().getSimpleName().equals("CancelledState")) {
                        activeTickets.add(t);
                    }
                }

                System.out.println("\n=== MY TICKETS ===");
                if (activeTickets.isEmpty()) {
                    System.out.println("You do not have any active purchased tickets yet.");
                } else {
                    for (Ticket t : activeTickets) {
                        String movieTitle = "Unknown Movie";
                        String hallId = "Unknown Hall";
                        String time = "??:00";

                        for (Session s : sessionService.getAllSessions()) {
                            if (s.getId().equals(t.getSessionId())) {
                                hallId = s.getHallId();
                                time = s.getStartHour() + ":00";

                                Movie m = movieService.getMovieById(s.getMovieId());
                                if (m != null) {
                                    movieTitle = m.getTitle();
                                }
                                break;
                            }
                        }

                        System.out.println("- Ticket No: " + t.getId() + " | Movie: " + movieTitle +
                                " | Hall: " + hallId + " | Time: " + time + " | Seat: " + t.getSeatId());
                    }

                    System.out.print("\nEnter the code of the ticket you want to cancel/refund (Type 'q' to return to main menu): ");
                    String ticketCode = scanner.nextLine();

                    if (!ticketCode.equalsIgnoreCase("q")) {
                        boolean isFound = false;
                        for (Ticket t : activeTickets) {
                            if (t.getId().equalsIgnoreCase(ticketCode)) {
                                ticketService.cancelTicket(t);
                                System.out.println("SUCCESS: Ticket " + ticketCode + " has been canceled and the seat is now available!");
                                isFound = true;
                                break;
                            }
                        }
                        if (!isFound) {
                            System.out.println("ERROR: No ticket found with the entered code!");
                        }
                    }
                }
                System.out.println("==================\n");
            }
            // 3. MOVIE SEARCH AND FILTERING
            else if (mainChoice >= 1 && mainChoice <= 3) {
                List<Movie> displayMovies = new ArrayList<>();

                if (mainChoice == 1) {
                    displayMovies = movieService.getAllMovies();
                }
                else if (mainChoice == 2) {
                    System.out.println("\nCategories: ACTION, DRAMA, COMEDY, THRILLER, HORROR, ANIMATION");
                    System.out.print("Enter the genre you want to filter by: ");
                    try {
                        Genre selectedGenre = Genre.valueOf(scanner.nextLine().toUpperCase());
                        displayMovies = movieService.getMoviesByGenre(selectedGenre);
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: Invalid category entered!");
                        continue;
                    }
                }
                else if (mainChoice == 3) {
                    System.out.print("\nMovie Title to Search (or a keyword): ");
                    String keyword = scanner.nextLine();
                    displayMovies = movieService.searchMoviesByTitle(keyword);
                }

                if (displayMovies.isEmpty()) {
                    System.out.println("\n[!] INFO: No movies found matching your criteria.");
                    continue;
                }

                System.out.println("\n--- MOVIE LIST ---");
                for (int i = 0; i < displayMovies.size(); i++) {
                    System.out.println((i + 1) + ". " + displayMovies.get(i).getTitle() + " (" + displayMovies.get(i).getGenre() + ")");
                }
                System.out.println((displayMovies.size() + 1) + ". Go Back");

                System.out.print("\nSelect the movie number you want to inspect: ");
                int movieChoice = scanner.nextInt();
                scanner.nextLine();

                if (movieChoice == displayMovies.size() + 1) {
                    continue;
                }

                if (movieChoice > 0 && movieChoice <= displayMovies.size()) {
                    Movie selectedMovie = displayMovies.get(movieChoice - 1);

                    System.out.println("\n--- MOVIE DETAILS ---");
                    System.out.println("Title: " + selectedMovie.getTitle());
                    System.out.println("Genre: " + selectedMovie.getGenre());
                    System.out.println("Duration: " + selectedMovie.getDurationMinutes() + " minutes");
                    System.out.println("IMDB Rating: " + selectedMovie.getRating());
                    System.out.println("Description: " + selectedMovie.getDescription());
                    System.out.println("----------------------");

                    System.out.println("1. View Sessions and Buy Ticket");
                    System.out.println("2. Return to Main Menu");
                    System.out.print("Your choice: ");
                    int actionChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (actionChoice == 1) {
                        List<Session> availableSessions = sessionService.getSessionsByMovie(selectedMovie.getId());
                        if (availableSessions.isEmpty()) {
                            System.out.println("\n[!] INFO: No sessions have been scheduled for this movie yet.");
                            continue;
                        }

                        System.out.println("\n--- AVAILABLE SESSIONS ---");
                        for (int i = 0; i < availableSessions.size(); i++) {
                            Session s = availableSessions.get(i);
                            System.out.println((i + 1) + ". Hall: " + s.getHallId() + " | Time: " + s.getStartHour() + ":00 | Price: " + s.getPricePerSeat() + " TL");
                        }
                        System.out.print("Please select a session: ");
                        int sChoice = scanner.nextInt();
                        scanner.nextLine();

                        if (sChoice > 0 && sChoice <= availableSessions.size()) {
                            Session targetSession = availableSessions.get(sChoice - 1);

                            System.out.println("\n==========================================");
                            System.out.println("               [ S C R E E N ]            ");
                            System.out.println("==========================================");
                            System.out.println("Color Key: [A1]=Available | \u001B[31m[A1]\u001B[0m=Occupied\n");

                            String[] rows = {"A", "B", "C", "D", "E", "F"};
                            for (String row : rows) {
                                for (int col = 1; col <= 10; col++) {
                                    String seatId = row + col;
                                    boolean isAvailable = ticketService.isSeatAvailable(targetSession.getId(), seatId);
                                    String formattedSeat = String.format("[%-3s]", seatId);

                                    if (isAvailable) {
                                        System.out.print(formattedSeat + " ");
                                    } else {
                                        System.out.print("\u001B[31m" + formattedSeat + "\u001B[0m ");
                                    }
                                }
                                System.out.println();
                            }
                            System.out.println("==========================================\n");

                            System.out.print("Select a seat (e.g., A5): ");
                            String seatId = scanner.nextLine().toUpperCase();

                            if (!ticketService.isSeatAvailable(targetSession.getId(), seatId)) {
                                System.out.println("\nERROR: Seat " + seatId + " is already occupied for this session!");
                                continue;
                            }

                            int rateChoice = 0;
                            boolean cancelBooking = false;

                            while (true) {
                                System.out.println("\n--- TARIFF SELECTION ---");
                                System.out.println("1. Standard Ticket");
                                System.out.println("2. DEU CENG Student (20% Discount)");
                                System.out.println("3. Cancel Transaction (Return to Main Menu)");
                                System.out.print("Your choice: ");
                                rateChoice = scanner.nextInt();
                                scanner.nextLine();

                                if (rateChoice == 3) {
                                    cancelBooking = true;
                                    break;
                                }
                                else if (rateChoice == 2) {
                                    System.out.print("Please enter your 10-digit DEU CENG Student Number (Type 'q' to go back): ");
                                    String studentNo = scanner.nextLine();

                                    if (studentNo.equalsIgnoreCase("q")) {
                                        continue;
                                    }

                                    if (studentNo.matches("2024510\\d{3}")) {
                                        System.out.println("SUCCESS: DEU CENG student verification approved!");
                                        break;
                                    } else {
                                        System.out.println("ERROR: Verification failed! Only 10-digit numbers starting with '2024510' are valid.");
                                    }
                                }
                                else if (rateChoice == 1) {
                                    break;
                                }
                                else {
                                    System.out.println("ERROR: Invalid choice!");
                                }
                            }

                            if (cancelBooking) {
                                System.out.println("Ticket purchasing process canceled.");
                                continue;
                            }

                            String ticketId = "T-" + (int)(Math.random() * 9000 + 1000);
                            Ticket ticket = new Ticket(ticketId, targetSession.getId(), seatId, user.getId());

                            System.out.println("\n--- PAYMENT METHOD ---");
                            System.out.println("1. Pay with Saved Card (Enter Existing Card Number)");
                            System.out.println("2. Add New Card and Pay");
                            System.out.print("Your choice: ");
                            int payChoice = scanner.nextInt();
                            scanner.nextLine();

                            String cardNumberToUse = "";

                            if (payChoice == 2) {
                                System.out.println("\n--- NEW CARD DETAILS ---");
                                System.out.print("Enter 16-Digit Card Number: ");
                                cardNumberToUse = scanner.nextLine();

                                System.out.print("Bank Name: ");
                                String bank = scanner.nextLine();

                                System.out.print("Balance to Load (TL): ");
                                double balance = scanner.nextDouble();
                                scanner.nextLine();

                                CreditCard newCard = new CreditCard(cardNumberToUse, user.getName(), bank, balance);
                                paymentService.addCreditCard(newCard);
                            } else {
                                System.out.print("Enter Credit Card Number (e.g., 1111222233334444): ");
                                cardNumberToUse = scanner.nextLine();
                            }

                            if (rateChoice == 2) {
                                bookingFacade.completePayment(ticket, new StudentPricing(), targetSession.getPricePerSeat(), cardNumberToUse);
                            } else {
                                bookingFacade.completePayment(ticket, new StandardPricing(), targetSession.getPricePerSeat(), cardNumberToUse);
                            }
                        }
                    }
                } else {
                    System.out.println("ERROR: Invalid choice!");
                }
            } else {
                System.out.println("ERROR: Invalid choice!");
            }
        }
    }
}