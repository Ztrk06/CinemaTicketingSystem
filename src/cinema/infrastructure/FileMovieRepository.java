package cinema.infrastructure;

import cinema.domain.Movie;
import cinema.domain.Genre;
import cinema.service.IMovieRepository;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * File-based implementation of the Movie Repository.
 * Handles persistent storage of movie data using a CSV file.
 */
public class FileMovieRepository implements IMovieRepository {
    private final String filePath = "data/movies.csv";

    public FileMovieRepository() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves all movies from the CSV file.
     *
     * @return A list of all stored movies.
     */
    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Movie movie = new Movie(
                            parts[0], // id
                            parts[1], // title
                            Genre.valueOf(parts[2].toUpperCase()), // genre
                            Integer.parseInt(parts[3]), // duration
                            Double.parseDouble(parts[4]), // rating
                            parts[5]  // description
                    );
                    movies.add(movie);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Finds a specific movie by its unique identifier.
     *
     * @param id The unique ID of the movie to search for.
     * @return The Movie object if found, otherwise null.
     */
    @Override
    public Movie findById(String id) {
        for (Movie m : findAll()) {
            if (m.getId().equals(id)) return m;
        }
        return null;
    }

    /**
     * Saves a new movie by appending it to the CSV file.
     *
     * @param movie The movie object to save.
     */
    @Override
    public void save(Movie movie) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(movie.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}