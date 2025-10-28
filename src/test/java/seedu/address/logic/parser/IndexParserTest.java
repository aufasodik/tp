package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.parser.IndexParser.MESSAGE_DUPLICATE_INDICES;
import static seedu.address.logic.parser.IndexParser.MESSAGE_INVALID_INDICES;
import static seedu.address.logic.parser.IndexParser.MESSAGE_INVALID_RANGE_ORDER;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.ParseIndicesException;

/**
 * Unit tests for {@link IndexParser}.
 * Covers all valid, invalid, and duplicate scenarios for single, comma-separated, and range-based indices.
 */
public class IndexParserTest {

    // ================================================================
    // POSITIVE CASES (SUCCESSFUL PARSING)
    // ================================================================

    @Test
    public void parseIndices_singleIndex_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("1");
        assertEquals(List.of(Index.fromOneBased(1)), indices);
    }

    @Test
    public void parseIndices_validCommaSeparated_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("1, 3, 5");
        assertEquals(List.of(
                Index.fromOneBased(1),
                Index.fromOneBased(3),
                Index.fromOneBased(5)), indices);
    }

    @Test
    public void parseIndices_validRange_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("2-4");
        assertEquals(List.of(
                Index.fromOneBased(2),
                Index.fromOneBased(3),
                Index.fromOneBased(4)), indices);
    }

    @Test
    public void parseIndices_validMixedRange_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("1,3-5,7");
        assertEquals(List.of(
                Index.fromOneBased(1),
                Index.fromOneBased(3),
                Index.fromOneBased(4),
                Index.fromOneBased(5),
                Index.fromOneBased(7)), indices);
    }

    @Test
    public void parseIndices_singleElementRange_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("3-3");
        assertEquals(List.of(Index.fromOneBased(3)), indices);
    }

    @Test
    public void parseIndices_spacesAroundDash_success() throws Exception {
        List<Index> indices = IndexParser.parseIndices("1 - 3");
        assertEquals(List.of(
                Index.fromOneBased(1),
                Index.fromOneBased(2),
                Index.fromOneBased(3)), indices);
    }

    // ================================================================
    // DUPLICATE DETECTION TESTS
    // ================================================================

    @Test
    public void parseIndices_duplicate_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "1"), () ->
                IndexParser.parseIndices("1,2,1"));
    }

    @Test
    public void parseIndices_multipleDuplicates_reportsAscendingOrder() {
        // duplicates: 1, 3
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "1, 3"), () ->
                IndexParser.parseIndices("3,1,2,3,1"));
    }

    @Test
    public void parseIndices_overlappingRanges_throwsParseIndicesException() {
        // overlap between 1-3 and 2-4 => duplicates 2,3
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "2, 3"), () ->
                IndexParser.parseIndices("1-3,2-4"));
    }

    @Test
    public void parseIndices_identicalRanges_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "1, 2, 3"), () ->
                IndexParser.parseIndices("1-3,1-3"));
    }

    @Test
    public void parseIndices_rangeAndSingleOverlap_throwsParseIndicesException() {
        // 1-3 creates 1,2,3 then 2 repeats
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "2"), () ->
                IndexParser.parseIndices("1-3,2"));
    }

    @Test
    public void parseIndices_complexMixedDuplicates_reportsAllAscending() {
        // 1,2-4,3,5-7,6 => duplicates 3,6
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "3, 6"), () ->
                IndexParser.parseIndices("1,2-4,3,5-7,6"));
    }

    @Test
    public void parseIndices_rangeContainedInAnother_throwsParseIndicesException() {
        // 1-5 and 2-4 overlap => duplicates 2,3,4
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "2, 3, 4"), () ->
                IndexParser.parseIndices("1-5,2-4"));
    }

    @Test
    public void parseIndices_singleElementRangeWithDuplicate_throwsParseIndicesException() {
        // 3-3 creates 3, then 3 repeats
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_DUPLICATE_INDICES, "3"), () ->
                IndexParser.parseIndices("3-3,3"));
    }

    // ================================================================
    // INVALID INPUT & SYNTAX TESTS
    // ================================================================

    @Test
    public void parseIndices_emptyString_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices(""));
    }

    @Test
    public void parseIndices_onlySpaces_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("   "));
    }

    @Test
    public void parseIndices_invalidCharacters_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("a"));
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1-a"));
    }

    @Test
    public void parseIndices_incompleteRanges_throwsParseIndicesException() {
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("3-"));
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("-3"));
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1--3"));
    }

    @Test
    public void parseIndices_reverseRange_throwsParseIndicesException() {
        // 5-2 (end < start) should be invalid
        assertThrows(ParseIndicesException.class, String.format(MESSAGE_INVALID_RANGE_ORDER, 5, 2), () ->
                IndexParser.parseIndices("5-2"));
    }

    @Test
    public void parseIndices_leadingTrailingCommasAndSpaces_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices(",1,2"));
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,2,"));
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,,2"));
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1, ,2"));
    }

    @Test
    public void parseIndices_invalidThenDuplicate_reportsInvalidFirst() {
        // "abc" invalid => invalid should be prioritized over duplicates
        assertThrows(ParseException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("abc,1,2,1"));
    }

    @Test
    public void parseIndices_zeroIndex_throwsParseIndicesException() {
        // Zero is invalid since indices are one-based
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("0"));
    }

    @Test
    public void parseIndices_negativeIndex_throwsParseIndicesException() {
        // Negative index should be invalid
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("-1"));
    }

    @Test
    public void parseIndices_mixedValidAndZeroOrNegative_throwsParseIndicesException() {
        // 0 and -1 mixed with valid indices
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,0,2"));
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,-3,4"));
    }

    // Test overflow/large values
    @Test
    public void parseIndices_largeNumberOverflow_throwsParseIndicesException() {
        // Larger than Integer.MAX_VALUE (~2.1 billion)
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("999999999999"));
    }

    @Test
    public void parseIndices_largeNumberInRange_throwsParseIndicesException() {
        // Overflow during range expansion (start or end too large)
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1-999999999999"));
    }

    @Test
    public void parseIndices_mixedValidAndOverflow_throwsParseIndicesException() {
        // Mixed valid small indices and overflowed large index
        assertThrows(ParseIndicesException.class, MESSAGE_INVALID_INDICES, () ->
                IndexParser.parseIndices("1,2,999999999999"));
    }
}

