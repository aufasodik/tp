package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;



/**
 * Parses input arguments and creates a new DeleteCommand object.
 * Supports:
 *   - Single index: "1"
 *   - Multiple indices: "1 3 5"
 *   - Ranges: "2-4" (inclusive)
 *   - Mixed: "1 3-5 7"
 * Duplicates are ignored (first occurrence kept).
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    private static ParseException invalidFormat() {
        return new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    private static int toZeroBasedIndex(String token) throws ParseException {
        // Map any parse error to the standard invalid-format message
        try {
            return ParserUtil.parseIndex(token).getZeroBased();
        } catch (ParseException e) {
            throw invalidFormat();
        }
    }

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        final String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw invalidFormat();
        }

        final String[] tokens = trimmed.split("\\s+");
        final Set<Integer> zeroBasedSet = new LinkedHashSet<>(); // preserve order, dedupe

        for (String tok : tokens) {
            if (tok.isBlank()) {
                continue;
            }

            if (tok.contains("-")) {
                // Range: a-b
                final String[] parts = tok.split("-", -1); // keep empties to catch "3-" / "-3"
                if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                    throw invalidFormat();
                }

                final int s;
                final int e;

                try {
                    s = ParserUtil.parseIndex(parts[0].trim()).getZeroBased();
                    e = ParserUtil.parseIndex(parts[1].trim()).getZeroBased();
                } catch (ParseException ex) {
                    // non-numeric / bad index -> standard invalid format
                    throw invalidFormat();
                }

                if (e < s) { // disallow "2-1"
                    throw invalidFormat();
                }

                for (int i = s; i <= e; i++) {
                    zeroBasedSet.add(i);
                }
            } else {
                // Single index token
                zeroBasedSet.add(toZeroBasedIndex(tok));
            }
        }

        if (zeroBasedSet.isEmpty()) {
            throw invalidFormat();
        }

        final List<Index> indices = new ArrayList<>(zeroBasedSet.size());
        for (int zb : zeroBasedSet) {
            indices.add(Index.fromZeroBased(zb));
        }

        return new DeleteCommand(indices);
    }
}
