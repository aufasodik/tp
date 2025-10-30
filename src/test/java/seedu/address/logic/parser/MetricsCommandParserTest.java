package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.MetricsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests for metrics command parsing to verify that extraneous parameters
 * are properly ignored and the command behaves consistently.
 */
public class MetricsCommandParserTest {

    private AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_metricsOnly_success() throws Exception {
        Command command = parser.parseCommand("metrics");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithSpaces_success() throws Exception {
        Command command = parser.parseCommand("metrics   ");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithNumber_ignoresExtraneousParameter() throws Exception {
        Command command = parser.parseCommand("metrics 3");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithPlus_ignoresExtraneousParameter() throws Exception {
        Command command = parser.parseCommand("metrics +");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsEquals_ignoresExtraneousParameter() throws Exception {
        Command command = parser.parseCommand("metrics = metrics");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithMultipleWords_ignoresExtraneousParameters() throws Exception {
        Command command = parser.parseCommand("metrics hello world 123");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithSpecialCharacters_ignoresExtraneousParameters() throws Exception {
        Command command = parser.parseCommand("metrics !@#$%^&*()");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithEqualsSign_ignoresExtraneousParameters() throws Exception {
        Command command = parser.parseCommand("metrics = = = test");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithFlags_ignoresExtraneousParameters() throws Exception {
        Command command = parser.parseCommand("metrics -a -b --verbose");
        assertTrue(command instanceof MetricsCommand);
    }

    @Test
    public void parseCommand_metricsWithLongArgument_ignoresExtraneousParameters() throws Exception {
        String longArg = "a".repeat(1000);
        Command command = parser.parseCommand("metrics " + longArg);
        assertTrue(command instanceof MetricsCommand);
    }

    /**
     * Test that all metrics commands with different extraneous parameters
     * produce equivalent MetricsCommand objects in behavior (though not in identity).
     */
    @Test
    public void parseCommand_differentExtraneousParams_produceSameBehavior() throws Exception {
        Command command1 = parser.parseCommand("metrics");
        Command command2 = parser.parseCommand("metrics 3");
        Command command3 = parser.parseCommand("metrics +");
        Command command4 = parser.parseCommand("metrics = metrics");
        Command command5 = parser.parseCommand("metrics hello world");

        // All should be MetricsCommand instances
        assertTrue(command1 instanceof MetricsCommand);
        assertTrue(command2 instanceof MetricsCommand);
        assertTrue(command3 instanceof MetricsCommand);
        assertTrue(command4 instanceof MetricsCommand);
        assertTrue(command5 instanceof MetricsCommand);

        // All should have the same class
        assertEquals(command1.getClass(), command2.getClass());
        assertEquals(command1.getClass(), command3.getClass());
        assertEquals(command1.getClass(), command4.getClass());
        assertEquals(command1.getClass(), command5.getClass());
    }

    /**
     * Test edge cases with various whitespace and parameter combinations.
     */
    @Test
    public void parseCommand_edgeCasesWithWhitespace_success() throws Exception {
        // Multiple spaces
        Command command1 = parser.parseCommand("metrics     123     abc");
        assertTrue(command1 instanceof MetricsCommand);

        // Leading/trailing whitespace with parameters
        Command command2 = parser.parseCommand("   metrics extra params   ");
        assertTrue(command2 instanceof MetricsCommand);

        // Extra whitespace between command and parameters
        Command command3 = parser.parseCommand("metrics    hello    world");
        assertTrue(command3 instanceof MetricsCommand);
    }

    /**
     * Test that the command word must be exactly "metrics" - case sensitive.
     */
    @Test
    public void parseCommand_wrongCase_throwsParseException() {
        try {
            parser.parseCommand("METRICS");
            assertTrue(false, "Should have thrown ParseException for wrong case");
        } catch (ParseException e) {
            // Expected - command is case sensitive
            assertTrue(true);
        }

        try {
            parser.parseCommand("Metrics");
            assertTrue(false, "Should have thrown ParseException for wrong case");
        } catch (ParseException e) {
            // Expected - command is case sensitive
            assertTrue(true);
        }
    }

    /**
     * Test that commands starting with "metrics" but with typos don't work.
     */
    @Test
    public void parseCommand_typos_throwsParseException() {
        try {
            parser.parseCommand("metric");
            assertTrue(false, "Should have thrown ParseException for typo");
        } catch (ParseException e) {
            // Expected - wrong command word
            assertTrue(true);
        }

        try {
            parser.parseCommand("metricss");
            assertTrue(false, "Should have thrown ParseException for typo");
        } catch (ParseException e) {
            // Expected - wrong command word
            assertTrue(true);
        }
    }
}
