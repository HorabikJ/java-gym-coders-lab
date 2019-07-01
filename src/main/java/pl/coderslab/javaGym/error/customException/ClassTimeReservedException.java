package pl.coderslab.javaGym.error.customException;

public class ClassTimeReservedException extends RuntimeException {

    private String message = "*Time that you want to reserve is already reserved.";

    public ClassTimeReservedException() {

    }

    public ClassTimeReservedException(String message) {
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
