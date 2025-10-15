package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;



/**
 * Parses input arguments and creates a new DeleteCommand object.
 * Supports:
 *   - Single index: "1"
 *   - Multiple indices: "1,3,5"
 *   - Range: "1-3"
 * Duplicates are ignored (first occurrence kept).
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {
    @Override
    public DeleteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        try {
            final List<Index> indices = ParserUtil.parseIndices(args);
            return new DeleteCommand(indices);
        } catch (ParseException pe) {
            if (pe.getMessage().equals(MESSAGE_INVALID_COMMAND_FORMAT)) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            } else {
                throw pe; // Preserve specific parsing errors
            }
        }
    }
}
