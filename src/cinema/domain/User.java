package cinema.domain;

public class User {
    private String id;
    private String name;
    private String email;
    private String passwordHash;

    public User(String id, String name, String email, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }

    public String toCSV() {
        return id + "," + name + "," + email + "," + passwordHash;
    }
}