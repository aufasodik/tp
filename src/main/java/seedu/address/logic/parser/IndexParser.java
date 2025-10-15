package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.exceptions.ParseIndicesException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 * Supports:
 * - Single index: "1"
 * - Multiple indices: "1,3,5"
 * Duplicates are ignored (first occurrence kept).
 */
public class IndexParser {
    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer: 1, 2, 3...";
    public static final String MESSAGE_INVALID_INDICES = "Indices must be a comma-separated list of positive integers"
            + " (1, 2, 3) or a range (1-3) or a combination of both (1, 2-4).";
    public static final String MESSAGE_DUPLICATE_INDICES = "Duplicate indices found: %1$s. "
            + "Each index should appear only once.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading
     * and trailing whitespaces will be
     * trimmed. Range validation should be done separately in the command.
     *
     * @throws ParseIndicesException if the specified index is invalid (not non-zero
     *                        unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses comma-separated indices into a {@code List<Index>} without range
     * validation.
     * Use this when range validation will be done later in the command execution.
     *
     * @param indicesString String containing comma-separated indices (e.g., "1",
     *                      "1,2,3")
     * @return List of valid unique indices
     * @throws ParseIndicesException if any index is invalid or duplicate
     */
    public static List<Index> parseIndices(String indicesString) throws ParseException, ParseIndicesException {
        requireNonNull(indicesString);
        String trimmedIndices = indicesString.trim();

        if (trimmedIndices.isEmpty()) {
            throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
        }

        String[] indexStrings = trimmedIndices.split(",", -1);
        List<Index> indexList = new ArrayList<>();
        Set<Integer> seenIndices = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        @FunctionalInterface
        interface Function2<One, Two> {
            public Two apply(One one, Two two);
        }

        Function2<Integer, String> addIndex = (Integer idx, String trimmedIndexString) -> {
            if (seenIndices.contains(idx)) {
                duplicates.add(trimmedIndexString);
            } else {
                seenIndices.add(idx);
                indexList.add(Index.fromOneBased(idx));
            }
            return null;
        };

        for (String indexString : indexStrings) {
            String trimmedIndexString = indexString.trim();
            if (trimmedIndexString.isEmpty()) {
                throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
            }

            if (trimmedIndexString.contains("-")) {
                Range range = parseRangeToken(trimmedIndexString);
                for (int i = range.start; i <= range.end; i++) {
                    int oneBasedIndex = i + 1;
                    addIndex.apply(oneBasedIndex, trimmedIndexString);
                }
            } else {
                if (trimmedIndexString.contains(" ")) {
                    throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
                }
                Index index;
                try {
                    index = parseIndex(trimmedIndexString);
                } catch (ParseException pe) {
                    throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
                }
                int oneBasedIndex = index.getOneBased();
                addIndex.apply(oneBasedIndex, trimmedIndexString);
            }

        }

        // Only duplicate checking gets the specific exception
        if (!duplicates.isEmpty()) {
            throw new ParseIndicesException(String.format(MESSAGE_DUPLICATE_INDICES,
                    String.join(", ", duplicates)));
        }

        return indexList;
    }

    /** Parses a token like "a-b" (inclusive) into a {@link Range}. */
    private static Range parseRangeToken(String token) throws ParseIndicesException {
        // keep empties to catch "3-" / "-3"
        final String[] parts = token.split("-", -1);

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
        }

        final int start;
        final int end;
        try {
            start = parseIndex(parts[0].trim()).getZeroBased();
            end = parseIndex(parts[1].trim()).getZeroBased();
        } catch (ParseException pe) {
            throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
        }

        // disallow "4-2"
        if (end < start) {
            throw new ParseIndicesException(MESSAGE_INVALID_INDICES);
        }
        return new Range(start, end);
    }

    /** Simple inclusive integer range. */
    private static final class Range {
        final int start;
        final int end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
