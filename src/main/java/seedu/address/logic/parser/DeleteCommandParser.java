package seedu.address.logic.parser;

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

    @Override
    public DeleteCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmed.split("\\s+");
        Set<Integer> zeroBasedSet = new LinkedHashSet<>(); // preserve insertion order, dedupe

        for (String token : tokens) {
            if (token.contains("-")) {
                // Range: a-b
                String[] parts = token.split("-", -1);
                if (parts.length != 2) {
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
                }
                Index start = ParserUtil.parseIndex(parts[0].trim());
                Index end = ParserUtil.parseIndex(parts[1].trim());
                int s = start.getZeroBased();
                int e = end.getZeroBased();
                if (e < s) {
                    // allow "4-2" by swapping? Keep strict & user-friendly: invalid format
                    throw new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
                }
                for (int i = s; i <= e; i++) {
                    zeroBasedSet.add(i);
                }
            } else {
                // Single index
                Index idx = ParserUtil.parseIndex(token);
                zeroBasedSet.add(idx.getZeroBased());
            }
        }

        if (zeroBasedSet.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        // Convert to 1-based Index list (ascending due to insertion order from tokens; DeleteCommand normalizes anyway)
        List<Index> indices = new ArrayList<>();
        for (Integer zb : zeroBasedSet) {
            indices.add(Index.fromZeroBased(zb));
        }

        return new DeleteCommand(indices);
    }
}
