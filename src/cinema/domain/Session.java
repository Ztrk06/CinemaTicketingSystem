package cinema.domain;

public class Session {
    private String id;
    private String movieId;
    private String hallId;
    private int startHour;
    private double pricePerSeat;

    public Session(String id, String movieId, String hallId, int startHour, double pricePerSeat) {
        this.id = id;
        this.movieId = movieId;
        this.hallId = hallId;
        this.startHour = startHour;
        this.pricePerSeat = pricePerSeat;
    }

    public String getId() { return id; }
    public String getMovieId() { return movieId; }
    public String getHallId() { return hallId; }
    public int getStartHour() { return startHour; }
    public double getPricePerSeat() { return pricePerSeat; }


    public String toCSV() {
        return id + "," + movieId + "," + hallId + "," + startHour + "," + pricePerSeat;
    }
}