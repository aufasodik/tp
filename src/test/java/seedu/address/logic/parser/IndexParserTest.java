package seedu.address.logic.parser;

import static seedu.address.logic.parser.IndexParser.MESSAGE_DUPLICATE_INDICES;
import static seedu.address.logic.parser.IndexParser.MESSAGE_INVALID_INDICES;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.ParseIndicesException;

public class IndexParserTest {
    @Test
    public void parseIndices_duplicateAtStart_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "1"), () ->
                IndexParser.parseIndices("1,2,3,1"));
    }

    @Test
    public void parseIndices_duplicateAtEnd_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "3"), () ->
                IndexParser.parseIndices("1,2,3,3"));
    }

    @Test
    public void parseIndices_multipleDuplicatesInOrder_throwsParseIndicesException() {
        // Should report all duplicates in the order they appear
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "2, 4"), () ->
                IndexParser.parseIndices("1,2,3,2,4,5,4"));
    }

    @Test
    public void parseIndices_invalidCharacters_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_INVALID_INDICES, "1"), () ->
                IndexParser.parseIndices("a"));
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_INVALID_INDICES, "1"), () ->
                IndexParser.parseIndices("1-a"));
    }

    @Test
    public void parseIndices_mixedValidInvalidAndDuplicate_throwsParseExceptionForInvalidFirst() {
        // Invalid format should be caught before duplicate checking
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("abc,1,2,1"));

        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,0,2,2"));
    }

    @Test
    public void parseIndices_leadingTrailingCommasAndSpaces_throwsParseException() {
        // Leading comma
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices(",1,2"));

        // Trailing comma
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,2,"));

        // Multiple consecutive commas
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,,2"));

        // Space only between commas
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1, ,2"));
    }
}
