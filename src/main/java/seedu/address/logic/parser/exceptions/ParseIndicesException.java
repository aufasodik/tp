package seedu.address.logic.parser.exceptions;

/**
 * Represents a parse error encountered when parsing indices specifically.
 * This allows distinguishing between general parse errors and specific indices parsing errors.
 */
public class ParseIndicesException extends ParseException {

    public ParseIndicesException(String message) {
        super(message);
    }

    public ParseIndicesException(String message, Throwable cause) {
        super(message, cause);
    }
}
