package pl.coderslab.javaGym.error.customException;

public class DomainObjectException extends RuntimeException {

    private String message = "*Provided email is already registered in database.";

    public DomainObjectException() { }

    public DomainObjectException(String message) {
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
