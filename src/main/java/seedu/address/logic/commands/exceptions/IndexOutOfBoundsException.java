package seedu.address.logic.commands.exceptions;

/**
 * Represents an error when a user-provided index is out of bounds.
 */
public class IndexOutOfBoundsException extends CommandException {

    private static final String MESSAGE_EMPTY_LIST = "Index out of bounds: The company list is empty.";
    private static final String MESSAGE_INVALID_INDEX = "Index out of bounds: %d. Index must be greater than 0.";
    private static final String MESSAGE_OUT_OF_BOUNDS = "Index out of bounds: %d. Valid range: 1 to %d.";

    /**
     * Constructs an IndexOutOfBoundsException with a simple error message.
     *
     * @param invalidIndex the 1-based index that was out of bounds
     * @param listSize the number of companies in the current list
     */
    public IndexOutOfBoundsException(int invalidIndex, int listSize) {
        super(createErrorMessage(invalidIndex, listSize));
    }

    /**
     * Creates a simple error message for index out of bounds.
     */
    private static String createErrorMessage(int invalidIndex, int listSize) {
        if (listSize == 0) {
            return MESSAGE_EMPTY_LIST;
        }

        if (invalidIndex <= 0) {
            return String.format(MESSAGE_INVALID_INDEX, invalidIndex)
                    + String.format(" Valid range: 1 to %d.", listSize);
        }

        return String.format(MESSAGE_OUT_OF_BOUNDS, invalidIndex, listSize);
    }
}
