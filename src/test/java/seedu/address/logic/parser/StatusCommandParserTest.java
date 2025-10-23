package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.StatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.company.Status;

/**
 * Contains integration tests (interaction with the Model) and unit tests for StatusCommandParser.
 */
public class StatusCommandParserTest {

    private static final String MESSAGE_USAGE = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            StatusCommand.MESSAGE_USAGE);

    private final StatusCommandParser parser = new StatusCommandParser();

    /**
     * Parses a valid index and a canonical hyphenated status.
     */
    @Test
    public void parse_validArgs_hyphen_success() {
        // User input: "1 s/in-process"
        // Expected result: StatusCommand with index=1 and status="in-process"
        Index idx = Index.fromOneBased(1);
        Status status = new Status("in-process");
        StatusCommand expected = new StatusCommand(idx, status);

        assertParseSuccess(parser, "1 s/in-process", expected);
    }

    /**
     * Accepts underscore form and normalizes to the same status (parser delegates to Status normalization).
     */
    @Test
    public void parse_validArgs_underscore_success() {
        // User input: "2 s/hr_interview"
        // Expected result: StatusCommand with index=2 and status normalized to "hr-interview"
        Index idx = Index.fromOneBased(2);
        Status status = new Status("hr-interview"); // expected canonical
        StatusCommand expected = new StatusCommand(idx, status);

        assertParseSuccess(parser, "2 s/hr_interview", expected);
    }

    /**
     * Tolerates extra whitespace around tokens.
     */
    @Test
    public void parse_validArgs_whitespace_success() {
        // User input: "  3   s/applied   "
        // Expected result: StatusCommand with index=3 and status="applied"
        Index idx = Index.fromOneBased(3);
        Status status = new Status("applied");
        StatusCommand expected = new StatusCommand(idx, status);

        assertParseSuccess(parser, "  3   s/applied   ", expected);
    }

    /**
     * When multiple status prefixes are provided, the last value should be taken.
     */
    @Test
    public void parse_duplicateStatusPrefix_lastWins_success() {
        // User input: "1 s/applied s/offered"
        // Expected result: StatusCommand with index=1 and status="offered" (last prefix wins)
        Index idx = Index.fromOneBased(1);
        Status status = new Status("offered"); // last s/offered wins
        StatusCommand expected = new StatusCommand(idx, status);

        assertParseSuccess(parser, "1 s/applied s/offered", expected);
    }

    /**
     * Missing index should fail with the command usage message.
     */
    @Test
    public void parse_missingIndex_failure() {
        // User input: "s/in-process"
        // Expected result: Parse failure with MESSAGE_USAGE (missing index)
        assertParseFailure(parser, "s/in-process", MESSAGE_USAGE);
    }

    /**
     * Missing status prefix should fail with the command usage message.
     */
    @Test
    public void parse_missingStatusPrefix_failure() {
        // User input: "1"
        // Expected result: Parse failure with MESSAGE_USAGE (missing s/STATUS)
        assertParseFailure(parser, "1", MESSAGE_USAGE);
    }

    /**
     * Non-integer index should fail with the command usage message (propagated as pe).
     */
    @Test
    public void parse_invalidIndex_nonInteger_failure() {
        // User input: "one s/in-process"
        // Expected result: Parse failure with MESSAGE_USAGE (index must be a positive integer)
        assertParseFailure(parser, "one s/in-process", MESSAGE_USAGE);
    }

    /**
     * Zero index should fail with the command usage message (indices are 1-based).
     */
    @Test
    public void parse_invalidIndex_zero_failure() {
        // User input: "0 s/in-process"
        // Expected result: Parse failure with MESSAGE_USAGE (index must be >= 1)
        assertParseFailure(parser, "0 s/in-process", MESSAGE_USAGE);
    }

    /**
     * Negative index should fail with the command usage message.
     */
    @Test
    public void parse_invalidIndex_negative_failure() {
        // User input: "-3 s/in-process"
        // Expected result: Parse failure with MESSAGE_USAGE (index must be a positive integer)
        assertParseFailure(parser, "-3 s/in-process", MESSAGE_USAGE);
    }

    /**
     * Empty status value (e.g., "s/") should fail via Status/ParserUtil constraints.
     */
    @Test
    public void parse_emptyStatusValue_failure() {
        // User input: "1 s/"
        // Expected result: Parse failure with Status.MESSAGE_CONSTRAINTS (empty/invalid status)
        assertParseFailure(parser, "1 s/", Status.MESSAGE_CONSTRAINTS);
    }

    /**
     * Unknown status label should fail with Status constraints message.
     */
    @Test
    public void parse_invalidStatus_failure() {
        // User input: "1 s/not-a-valid-status"
        // Expected result: Parse failure with Status.MESSAGE_CONSTRAINTS (unknown status label)
        assertParseFailure(parser, "1 s/not-a-valid-status", Status.MESSAGE_CONSTRAINTS);
    }
}