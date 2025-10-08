package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.company.Remark;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * RemarkCommand and returns a RemarkCommand object for execution.
     * @param args the input arguments string containing the index and optional remark
     * @return a RemarkCommand object with the parsed index and remark
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        ParseException pe = new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE));
        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException e) {
            throw pe;
        }
        Optional<String> remarkStr = argMultimap.getValue(PREFIX_REMARK);
        Remark remark;
        if (remarkStr.isEmpty()) {
            throw pe;
        } else {
            remark = ParserUtil.parseRemark(remarkStr.get());
        }
        return new RemarkCommand(index, remark);
    }

}
