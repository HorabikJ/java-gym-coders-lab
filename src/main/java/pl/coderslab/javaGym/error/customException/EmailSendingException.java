package pl.coderslab.javaGym.error.customException;

public class EmailSendingException extends RuntimeException {

    private String message = "*There was a problem with email sending service.";

    public EmailSendingException() {}

    public EmailSendingException(String message) {
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
