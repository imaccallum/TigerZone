package exceptions;

public class BadServerInputException extends Exception {
    public BadServerInputException(String message) {
        super(message);
    }
}