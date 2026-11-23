package cinema.service;

import cinema.domain.Movie;
import java.util.List;

public interface IMovieRepository {
    List<Movie> findAll();
    Movie findById(String id);
    void save(Movie movie);
}