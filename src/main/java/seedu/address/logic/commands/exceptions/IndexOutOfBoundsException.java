package seedu.address.logic.commands.exceptions;

/**
 * Represents an error when a user-provided index is out of bounds.
 */
public class IndexOutOfBoundsException extends CommandException {

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
            return "Index out of bounds: The company list is empty.";
        }

        if (invalidIndex <= 0) {
            return String.format("Index out of bounds: %d. Index must be greater than 0. Valid range: 1 to %d.",
                    invalidIndex, listSize);
        }

        return String.format("Index out of bounds: %d. Valid range: 1 to %d.", invalidIndex, listSize);
    }
}
