package seedu.address.logic.commands.exceptions;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class IndexOutOfBoundsExceptionTest {

    @Test
    public void constructor_validIndexOutOfRange_correctMessage() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException(5, 3);
        String message = exception.getMessage();
        
        assertTrue(message.contains("Index out of bounds: 5"));
        assertTrue(message.contains("Valid range: 1 to 3"));
    }

    @Test
    public void constructor_negativeIndex_correctMessage() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException(-1, 5);
        String message = exception.getMessage();
        
        assertTrue(message.contains("Index out of bounds: -1"));
        assertTrue(message.contains("Index must be greater than 0"));
        assertTrue(message.contains("Valid range: 1 to 5"));
    }

    @Test
    public void constructor_zeroIndex_correctMessage() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException(0, 3);
        String message = exception.getMessage();
        
        assertTrue(message.contains("Index out of bounds: 0"));
        assertTrue(message.contains("Index must be greater than 0"));
        assertTrue(message.contains("Valid range: 1 to 3"));
    }

    @Test
    public void constructor_emptyList_correctMessage() {
        IndexOutOfBoundsException exception = new IndexOutOfBoundsException(1, 0);
        String message = exception.getMessage();
        
        assertTrue(message.contains("Index out of bounds: The company list is empty"));
    }
}