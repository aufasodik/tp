package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.company.Status;

/**
 * Parses input arguments and creates a new {@link FilterCommand} object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS);

        // No preamble expected; require s/STATUS present
        if (!argMultimap.getPreamble().trim().isEmpty() || argMultimap.getValue(PREFIX_STATUS).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        Status status = ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS).get());
        return new FilterCommand(status);
    }
}

