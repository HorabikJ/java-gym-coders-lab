package pl.coderslab.javaGym.error.customException;

public class NotAuthenticatedException extends RuntimeException {

    private String message = "*Not authenticated exception.";

    public NotAuthenticatedException() {}

    public NotAuthenticatedException(String message) {
        this.message = message;
    }

}
