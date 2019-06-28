package pl.coderslab.javaGym.error.customException;

public class ActionNotAllowedException extends RuntimeException{

    private String message = "*Action not allowed.";

    public ActionNotAllowedException() {}

    public ActionNotAllowedException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
