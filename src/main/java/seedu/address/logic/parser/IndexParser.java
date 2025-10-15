package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object.
 * Supports:
 * - Single index: "1"
 * - Multiple indices: "1,3,5"
 * Duplicates are ignored (first occurrence kept).
 */
public class IndexParser {
    /** Parses input arguments and creates a new List of Index objects. */
    public static List<Index> parse(String args) throws ParseException {
        requireNonNull(args);

        final String[] tokens = tokenize(args);
        final Set<Integer> zeroBased = collectZeroBased(tokens);
        if (zeroBased.isEmpty()) {
            throw invalidFormat();
        }

        return toIndexList(zeroBased);
    }

    /** Splits and validates the raw input into non-empty tokens. */
    private static String[] tokenize(String raw) throws ParseException {
        final String trimmed = raw.trim();
        return trimmed.split(",");
    }

    /**
     * Parses tokens into a de-duplicated, insertion-ordered set of zero-based
     * indices.
     */
    private static Set<Integer> collectZeroBased(String[] tokens) throws ParseException {
        final Set<Integer> out = new LinkedHashSet<>();
        for (String tok : tokens) {
            final String trimmed = tok.trim();
            if (trimmed.isBlank()) {
                continue;
            }
            if (tok.contains("-")) {
                Range r = parseRangeToken(trimmed);
                addRange(out, r.start, r.end);
            } else {
                out.add(parseIndexToken(trimmed));
            }
        }
        return out;
    }

    /** Converts a set of zero-based ints to a list of {@link Index}. */
    private static List<Index> toIndexList(Set<Integer> zeroBased) {
        final List<Index> indices = new ArrayList<>(zeroBased.size());
        for (int zb : zeroBased) {
            indices.add(Index.fromZeroBased(zb));
        }
        return indices;
    }

    /** Parses a token like "a-b" (inclusive) into a {@link Range}. */
    private static Range parseRangeToken(String token) throws ParseException {
        // keep empties to catch "3-" / "-3"
        final String[] parts = token.split("-", -1);

        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            throw invalidFormat();
        }

        final int start = parseIndexToken(parts[0].trim());
        final int end = parseIndexToken(parts[1].trim());

        // disallow "4-2"
        if (end < start) {
            throw invalidFormat();
        }
        return new Range(start, end);
    }

    /** Adds all indices in [start, end] (inclusive) to the set. */
    private static void addRange(Set<Integer> set, int start, int end) {
        for (int i = start; i <= end; i++) {
            set.add(i);
        }
    }

    /**
     * Parses a single index token to zero-based, mapping errors to a standard
     * invalid-format message.
     */
    private static int parseIndexToken(String token) throws ParseException {
        try {
            return ParserUtil.parseIndex(token).getZeroBased();
        } catch (ParseException e) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
    }

    private static ParseException invalidFormat() {
        return new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
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
