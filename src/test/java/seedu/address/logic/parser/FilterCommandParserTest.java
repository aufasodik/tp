package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.model.company.Status;
import seedu.address.model.tag.Tag;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", FilterCommandParser.MESSAGE_NO_FILTERS);
    }

    @Test
    public void parse_noPrefixes_throwsParseException() {
        // No status or tag prefixes provided - treated as preamble
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
        // Unknown status value
        assertParseFailure(parser, " s/unknown-stage", Status.MESSAGE_CONSTRAINTS);

        // Partial status name
        assertParseFailure(parser, " s/app", Status.MESSAGE_CONSTRAINTS);

        // Empty status value
        assertParseFailure(parser, " s/", Status.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        // Tag with special characters
        assertParseFailure(parser, " t/%^&$", Tag.MESSAGE_CONSTRAINTS);

        // Tag with spaces
        assertParseFailure(parser, " t/java script", Tag.MESSAGE_CONSTRAINTS);

        // Empty tag value
        assertParseFailure(parser, " t/", Tag.MESSAGE_CONSTRAINTS);

        // Tag with only spaces
        assertParseFailure(parser, " t/   ", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_duplicateStatusPrefix_throwsParseException() {
        assertParseFailure(parser, " s/applied s/offered",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STATUS));
    }

    @Test
    public void parse_statusOnly_returnsFilterCommand() {
        // Valid status filter
        FilterCommand expected = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        assertParseSuccess(parser, " s/applied", expected);

        // Status with underscores (normalized by Status)
        expected = new FilterCommand(
                Optional.of(new Status("hr-interview")), Collections.emptyList());
        assertParseSuccess(parser, " s/hr_interview ", expected);

        // Different status values
        expected = new FilterCommand(
                Optional.of(new Status("offered")), Collections.emptyList());
        assertParseSuccess(parser, " s/offered", expected);

        expected = new FilterCommand(
                Optional.of(new Status("tech-interview")), Collections.emptyList());
        assertParseSuccess(parser, " s/tech-interview", expected);
    }

    @Test
    public void parse_tagOnly_returnsFilterCommand() {
        // Single tag filter
        FilterCommand expected = new FilterCommand(
                Optional.empty(), Collections.singletonList("java"));
        assertParseSuccess(parser, " t/java", expected);

        // Tag with hyphens
        expected = new FilterCommand(
                Optional.empty(), Collections.singletonList("remote-work"));
        assertParseSuccess(parser, " t/remote-work", expected);

        // Multiple tag filters
        expected = new FilterCommand(
                Optional.empty(), Arrays.asList("java", "remote"));
        assertParseSuccess(parser, " t/java t/remote", expected);

        // Multiple tags with extra spaces
        expected = new FilterCommand(
                Optional.empty(), Arrays.asList("java", "remote", "good"));
        assertParseSuccess(parser, "  t/java  t/remote   t/good  ", expected);
    }

    @Test
    public void parse_statusAndTag_returnsFilterCommand() {
        // Status and single tag
        FilterCommand expected = new FilterCommand(
                Optional.of(new Status("applied")), Collections.singletonList("java"));
        assertParseSuccess(parser, " s/applied t/java", expected);

        // Status and multiple tags
        expected = new FilterCommand(
                Optional.of(new Status("offered")), Arrays.asList("java", "remote", "good"));
        assertParseSuccess(parser, " s/offered t/java t/remote t/good", expected);

        // Different order (tags before status)
        expected = new FilterCommand(
                Optional.of(new Status("applied")), Arrays.asList("java", "remote"));
        assertParseSuccess(parser, " t/java t/remote s/applied", expected);
    }

    @Test
    public void parse_multipleTags_duplicateKeywordsAllowed() {
        // Duplicate tag keywords should be allowed (might be intentional)
        FilterCommand expected = new FilterCommand(
                Optional.empty(), Arrays.asList("java", "java"));
        assertParseSuccess(parser, " t/java t/java", expected);
    }
}

