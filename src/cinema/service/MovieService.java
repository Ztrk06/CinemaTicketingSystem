package cinema.service;

import cinema.domain.Genre;
import cinema.domain.Movie;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class responsible for business logic related to Movies.
 * Handles operations such as retrieval, searching, and adding new movies.
 */
public class MovieService {
    private final IMovieRepository movieRepository;

    public MovieService(IMovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    /**
     * Retrieves all movies from the repository.
     *
     * @return A list of all available movies.
     */
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * Finds a specific movie by its unique identifier.
     *
     * @param id The unique ID of the movie.
     * @return The Movie object if found, otherwise null.
     */
    public Movie getMovieById(String id) {
        return movieRepository.findById(id);
    }

    /**
     * Adds a new movie to the repository.
     *
     * @param movie The movie to be added.
     */
    public void addMovie(Movie movie) {
        movieRepository.save(movie);
    }

    /**
     * Generates the next sequential movie ID (e.g., M001, M002).
     *
     * @return A newly generated unique movie ID string.
     */
    public String generateNextMovieId() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return "M001";
        }

        Movie lastMovie = movies.get(movies.size() - 1);
        String lastId = lastMovie.getId();

        try {
            // Extract the numeric part and increment it
            int numericPart = Integer.parseInt(lastId.substring(1));
            return String.format("M%03d", numericPart + 1);
        } catch (NumberFormatException e) {
            // Fallback ID generation if parsing fails
            return "M" + (movies.size() + 1);
        }
    }

    /**
     * Filters movies by their genre.
     *
     * @param genre The genre to filter by.
     * @return A list of movies matching the specified genre.
     */
    public List<Movie> getMoviesByGenre(Genre genre) {
        List<Movie> filtered = new ArrayList<>();
        for (Movie m : movieRepository.findAll()) {
            if (m.getGenre() == genre) {
                filtered.add(m);
            }
        }
        return filtered;
    }

    /**
     * Searches for movies by title using a case-insensitive keyword.
     *
     * @param keyword The search keyword.
     * @return A list of movies containing the keyword in their title.
     */
    public List<Movie> searchMoviesByTitle(String keyword) {
        List<Movie> result = new ArrayList<>();
        for (Movie m : movieRepository.findAll()) {
            if (m.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(m);
            }
        }
        return result;
    }
}