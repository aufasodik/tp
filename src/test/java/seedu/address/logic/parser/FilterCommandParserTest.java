package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.company.Status;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingStatusPrefix_throwsParseException() {
        assertParseFailure(parser, "applied",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_withPreamble_throwsParseException() {
        assertParseFailure(parser, "1 s/applied",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidStatus_throwsParseException() {
        // unknown token
        assertParseFailure(parser, "s/unknown-stage", Status.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        FilterCommand expected = new FilterCommand(new Status("applied"));
        assertParseSuccess(parser, "s/applied", expected);
        // underscores accepted and normalized by Status
        assertParseSuccess(parser, " s/hr_interview ", new FilterCommand(new Status("hr-interview")));
    }
}

