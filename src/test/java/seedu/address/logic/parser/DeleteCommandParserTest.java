package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_COMPANY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_COMPANY;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;

public class DeleteCommandParserTest {

    private final DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validSingleIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, "1", new DeleteCommand(List.of(INDEX_FIRST_COMPANY)));
    }

    @Test
    public void parse_validMultipleIndices_returnsDeleteCommand() {
        assertParseSuccess(parser, "1,2", new DeleteCommand(List.of(
                INDEX_FIRST_COMPANY, INDEX_SECOND_COMPANY)));
    }

    @Test
    public void parse_validRange_returnsDeleteCommand() {
        assertParseSuccess(parser, "1, 5,               6", new DeleteCommand(List.of(
                INDEX_FIRST_COMPANY,
                Index.fromOneBased(5),
                Index.fromOneBased(6))));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2-1",
                String.format(MESSAGE_INVALID_INDEX, "2-1"));
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_INDEX, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1 3 5",
                String.format(MESSAGE_INVALID_INDEX, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1,3 533",
                String.format(MESSAGE_INVALID_INDEX, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "1,3, , 533",
                String.format(MESSAGE_INVALID_INDEX, DeleteCommand.MESSAGE_USAGE));
    }
}
