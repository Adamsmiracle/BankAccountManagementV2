package src.models.exceptions;

public class OverdraftExceededException extends RuntimeException {
    public OverdraftExceededException(String message) {
        super(message);
    }
}
