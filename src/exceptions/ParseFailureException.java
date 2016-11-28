package exceptions;

public class ParseFailureException extends Exception {
    public ParseFailureException(String message) {
        super(message);
    }
}