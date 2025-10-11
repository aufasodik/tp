package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_COMPANY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_COMPANY;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteCommand;

public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(List.of(INDEX_FIRST_COMPANY)));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteCommand() {
        assertParseSuccess(parser, "1 2", new DeleteCommand(List.of(
                INDEX_FIRST_COMPANY, INDEX_SECOND_COMPANY)));
    }

    @Test
    public void parse_validRange_returnsDeleteCommand() {
        assertParseSuccess(parser, "1-2", new DeleteCommand(List.of(
                INDEX_FIRST_COMPANY, INDEX_SECOND_COMPANY)));
    }

    @Test
    public void parse_mixedIndicesAndRange_returnsDeleteCommand() {
        // 1 2-2 should dedupe to [1,2]
        assertParseSuccess(parser, "1 2-2", new DeleteCommand(List.of(
                INDEX_FIRST_COMPANY, INDEX_SECOND_COMPANY)));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "2-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
