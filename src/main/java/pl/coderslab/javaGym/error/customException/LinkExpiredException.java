package pl.coderslab.javaGym.error.customException;

public class LinkExpiredException extends RuntimeException {

    private String message = "*Your link has expired.";

    public LinkExpiredException() {}

    public LinkExpiredException(String message) {
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
