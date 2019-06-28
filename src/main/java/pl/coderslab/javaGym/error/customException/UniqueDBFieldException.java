package pl.coderslab.javaGym.error.customException;

public class UniqueDBFieldException extends RuntimeException {

    private String message = "*Provided value is already registered in DB. It has to be unique.";

    public UniqueDBFieldException() { }

    public UniqueDBFieldException(String message) {
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
