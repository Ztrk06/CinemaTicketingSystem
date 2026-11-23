package cinema.presentation;

import cinema.domain.Admin;
import cinema.domain.Genre;
import cinema.domain.Movie;
import cinema.domain.Session;
import cinema.service.MovieService;
import cinema.service.SessionService;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the console-based User Interface for Admin operations.
 * Allows administrators to add movies, create sessions with overlap validation,
 * and view all active sessions.
 */
public class AdminMenu {
    private final MovieService movieService;
    private final SessionService sessionService;

    public AdminMenu(MovieService movieService, SessionService sessionService) {
        this.movieService = movieService;
        this.sessionService = sessionService;
    }

    /**
     * Displays the interactive admin panel menu.
     *
     * @param admin The admin user currently logged in.
     */
    public void show(Admin admin) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== ADMIN PANEL ===");
            System.out.println("1. Add Movie");
            System.out.println("2. Create Session (Conflict Checked)");
            System.out.println("3. View All Sessions");
            System.out.println("4. Exit");
            System.out.print("Your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 4) break;

            if (choice == 1) {
                String id = movieService.generateNextMovieId();
                System.out.println("\n--- ADD NEW MOVIE ---");
                System.out.println("Movie ID: " + id);

                System.out.print("Title: ");
                String title = scanner.nextLine();

                // 1. GENRE VALIDATION
                System.out.println("Categories: ACTION, DRAMA, COMEDY, THRILLER, HORROR, ANIMATION");
                Genre genre = null;
                while (genre == null) {
                    System.out.print("Genre: ");
                    try {
                        genre = Genre.valueOf(scanner.nextLine().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("ERROR: Invalid category! Please enter one of the listed genres.");
                    }
                }

                // 2. DURATION VALIDATION
                int duration = 0;
                while (duration <= 0) {
                    System.out.print("Duration (Minutes): ");
                    duration = scanner.nextInt();
                    if (duration <= 0) {
                        System.out.println("ERROR: Movie duration must be greater than 0!");
                    }
                }

                // 3. RATING VALIDATION
                double rating = -1;
                while (rating < 0 || rating > 10) {
                    System.out.print("IMDB Rating (0-10, e.g., 8.5): ");
                    rating = scanner.nextDouble();
                    if (rating < 0 || rating > 10) {
                        System.out.println("ERROR: Rating must be between 0 and 10!");
                    }
                }
                scanner.nextLine(); // Consume the newline character

                System.out.print("Description (Please do not use commas): ");
                String description = scanner.nextLine();

                movieService.addMovie(new Movie(id, title, genre, duration, rating, description));
                System.out.println("\nSUCCESS: Movie has been saved to the system!");
            }
            else if (choice == 2) {
                System.out.println("\n--- CREATE SESSION ---");
                List<Movie> movies = movieService.getAllMovies();

                if (movies.isEmpty()) {
                    System.out.println("No movies in the system!");
                    continue;
                }

                for (int i = 0; i < movies.size(); i++) {
                    System.out.println((i + 1) + ". " + movies.get(i).getTitle());
                }

                System.out.print("Select a Movie: ");
                int mIdx = scanner.nextInt();
                scanner.nextLine();

                if (mIdx > 0 && mIdx <= movies.size()) {
                    Movie selectedMovie = movies.get(mIdx - 1);

                    System.out.print("Hall Name (Hall-1 ... Hall-6): ");
                    String hallId = scanner.nextLine();

                    System.out.print("Start Hour (Integer only, e.g., 14): ");
                    int startHour = scanner.nextInt();

                    // CHECK HALL AVAILABILITY
                    if (!sessionService.isHallAvailable(hallId, startHour, selectedMovie.getDurationMinutes())) {
                        System.out.println("\n[!] ERROR: " + hallId + " is already occupied during that time slot!");
                        System.out.println("Session creation canceled.");
                        continue;
                    }

                    System.out.print("Base Seat Price: ");
                    double price = scanner.nextDouble();

                    String sId = sessionService.generateNextSessionId();
                    sessionService.addSession(new Session(sId, selectedMovie.getId(), hallId, startHour, price));
                    System.out.println("\nSUCCESS: Session " + sId + " created for " + startHour + ":00.");
                }
            }
            else if (choice == 3) {
                System.out.println("\n--- ALL SYSTEM SESSIONS ---");
                List<Session> allSessions = sessionService.getAllSessions();

                if (allSessions.isEmpty()) {
                    System.out.println("No registered sessions found.");
                } else {
                    for (Session s : allSessions) {
                        Movie m = movieService.getMovieById(s.getMovieId());
                        String movieTitle = (m != null) ? m.getTitle() : "Unknown Movie";
                        System.out.println("- Session: " + s.getId() + " | Movie: " + movieTitle +
                                " | Hall: " + s.getHallId() + " | Time: " + s.getStartHour() +
                                ":00 | Price: " + s.getPricePerSeat() + " TL");
                    }
                }
            }
        }
    }
}