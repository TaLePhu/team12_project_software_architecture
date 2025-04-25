package iuh.se.team.webbookstore_backend.entities;

public class Notify {
    private String message;

    public Notify(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
