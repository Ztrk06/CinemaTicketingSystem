package cinema.domain;

public class Movie {
    private String id;
    private String title;
    private Genre genre;
    private int durationMinutes;
    private double rating;
    private String description;

    public Movie(String id, String title, Genre genre, int durationMinutes, double rating, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.description = description;
    }

    public String getId() { return id; }

    public String getTitle() { return title; }

    public Genre getGenre() { return genre; }

    public int getDurationMinutes() { return durationMinutes; }

    public double getRating() { return rating; }

    public String getDescription() { return description; }

    public String toCSV() {
        return id + "," + title + "," + genre + "," + durationMinutes + "," + rating + "," + description;
    }
}