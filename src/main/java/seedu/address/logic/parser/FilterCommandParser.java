package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.company.Status;

/**
 * Parses input arguments and creates a new {@link FilterCommand} object.
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    public static final String MESSAGE_NO_FILTERS = "At least one field to filter must be provided.\n"
            + FilterCommand.MESSAGE_USAGE;

    @Override
    public FilterCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_STATUS, PREFIX_TAG);

        // No preamble expected
        if (!argMultimap.getPreamble().trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
        }

        // Disallow duplicate status prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STATUS);

        // Parse and validate status (optional)
        Optional<Status> status = Optional.empty();
        if (argMultimap.getValue(PREFIX_STATUS).isPresent()) {
            status = Optional.of(ParserUtil.parseStatus(argMultimap.getValue(PREFIX_STATUS).get()));
        }

        // Parse and validate tag keywords (optional, can have multiple)
        List<String> tagKeywords = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG))
                .stream()
                .map(tag -> tag.tagName)
                .toList();

        // Ensure at least one filter is provided
        if (status.isEmpty() && tagKeywords.isEmpty()) {
            throw new ParseException(MESSAGE_NO_FILTERS);
        }

        return new FilterCommand(status, tagKeywords);
    }
}
