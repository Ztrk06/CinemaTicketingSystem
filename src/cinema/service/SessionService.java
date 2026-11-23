package cinema.service;

import cinema.domain.Movie;
import cinema.domain.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for managing movie sessions.
 * Handles session creation, retrieval, and hall availability checks to prevent scheduling conflicts.
 */
public class SessionService {
    private final ISessionRepository sessionRepository;
    private final MovieService movieService;

    public SessionService(ISessionRepository sessionRepository, MovieService movieService) {
        this.sessionRepository = sessionRepository;
        this.movieService = movieService;
    }

    /**
     * Adds a new session to the repository.
     *
     * @param session The session to be added.
     */
    public void addSession(Session session) {
        sessionRepository.save(session);
    }

    /**
     * Retrieves all scheduled sessions from the repository.
     *
     * @return A list of all sessions.
     */
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    /**
     * Retrieves all sessions for a specific movie.
     *
     * @param movieId The unique ID of the movie.
     * @return A list of sessions scheduled for the given movie.
     */
    public List<Session> getSessionsByMovie(String movieId) {
        List<Session> movieSessions = new ArrayList<>();
        for (Session s : sessionRepository.findAll()) {
            if (s.getMovieId().equals(movieId)) {
                movieSessions.add(s);
            }
        }
        return movieSessions;
    }

    /**
     * Checks if a specific hall is available for a new session without overlapping existing ones.
     * Calculates the duration in hours (rounded up) to ensure sufficient time slots.
     *
     * @param hallId               The ID of the hall to check.
     * @param newStartHour         The proposed starting hour for the new session.
     * @param movieDurationMinutes The duration of the movie in minutes.
     * @return True if the hall is available, false if there is a scheduling conflict.
     */
    public boolean isHallAvailable(String hallId, int newStartHour, int movieDurationMinutes) {
        // Calculate movie duration in hours, rounded up (e.g., 148 mins -> 3 hours)
        int newDurationHours = (int) Math.ceil(movieDurationMinutes / 60.0);
        int newEndHour = newStartHour + newDurationHours;

        for (Session s : sessionRepository.findAll()) {
            if (s.getHallId().equalsIgnoreCase(hallId)) {
                Movie existingMovie = movieService.getMovieById(s.getMovieId());
                if (existingMovie == null) continue;

                int existingStart = s.getStartHour();
                int existingDurationHours = (int) Math.ceil(existingMovie.getDurationMinutes() / 60.0);
                int existingEnd = existingStart + existingDurationHours;

                // Mathematical overlap check
                if (newStartHour < existingEnd && newEndHour > existingStart) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Generates the next sequential session ID (e.g., S001, S002).
     *
     * @return A newly generated unique session ID string.
     */
    public String generateNextSessionId() {
        List<Session> sessions = sessionRepository.findAll();
        if (sessions.isEmpty()) return "S001";

        Session lastSession = sessions.get(sessions.size() - 1);
        try {
            int numericPart = Integer.parseInt(lastSession.getId().substring(1));
            return String.format("S%03d", numericPart + 1);
        } catch (Exception e) {
            return "S" + (sessions.size() + 1);
        }
    }
}