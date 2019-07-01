package pl.coderslab.javaGym.error.customException;

public class ReservationException extends RuntimeException {

    private String message = "*Reservation exception.";

    public ReservationException() { }

    public ReservationException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
