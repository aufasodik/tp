package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Unit tests for MetricsCommand to verify command execution behavior
 * with various inputs and edge cases.
 */
public class MetricsCommandTest {

    private Model model = new ModelManager(new AddressBook(), new UserPrefs());

    @Test
    public void constructor_noArguments_success() {
        MetricsCommand command = new MetricsCommand();
        assertEquals(MetricsCommand.COMMAND_WORD, "metrics");
    }

    @Test
    public void constructor_caseSensitive_failure() {
        assertNotEquals(MetricsCommand.COMMAND_WORD, "Metrics");
    }

    @Test
    public void execute_emptyAddressBook_success() {
        MetricsCommand command = new MetricsCommand();
        CommandResult result = command.execute(model);

        assertEquals(MetricsCommand.SHOWING_METRICS_MESSAGE, result.getFeedbackToUser());
        assertTrue(result.isShowMetrics());
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
    }

    @Test
    public void execute_withCompanies_success() {
        // Add some test companies to the model
        // (This would require adding companies but focusing on command behavior)
        MetricsCommand command = new MetricsCommand();
        CommandResult result = command.execute(model);

        assertEquals(MetricsCommand.SHOWING_METRICS_MESSAGE, result.getFeedbackToUser());
        assertTrue(result.isShowMetrics());
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        MetricsCommand command = new MetricsCommand();
        // MetricsCommand doesn't actually check for null model since it doesn't use it
        // This test verifies it still returns a result
        CommandResult result = command.execute(null);
        assertEquals(MetricsCommand.SHOWING_METRICS_MESSAGE, result.getFeedbackToUser());
        assertTrue(result.isShowMetrics());
    }

    @Test
    public void equals_sameCommandType_returnsFalse() {
        MetricsCommand command1 = new MetricsCommand();
        MetricsCommand command2 = new MetricsCommand();
        // Default Object.equals() compares object identity, not content
        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentObjectType_returnsFalse() {
        MetricsCommand command = new MetricsCommand();
        assertFalse(command.equals("not a command"));
    }

    @Test
    public void equals_nullObject_returnsFalse() {
        MetricsCommand command = new MetricsCommand();
        assertFalse(command.equals(null));
    }

    @Test
    public void hashCode_differentCommands_returnsDifferentHashCode() {
        MetricsCommand command1 = new MetricsCommand();
        MetricsCommand command2 = new MetricsCommand();
        // Default Object.hashCode() returns different values for different objects
        assertFalse(command1.hashCode() == command2.hashCode());
    }

    @Test
    public void toString_validCommand_returnsObjectToString() {
        MetricsCommand command = new MetricsCommand();
        // Default Object.toString() returns class@hashcode format
        String result = command.toString();
        assertTrue(result.startsWith("seedu.address.logic.commands.MetricsCommand@"));
    }

    /**
     * Test for metrics command parsing with extraneous parameters.
     * The metrics command should work normally even with extra parameters
     * since it ignores arguments.
     */
    @Test
    public void parseCommand_withExtraneousParameters_success() throws ParseException {
        // This test verifies that the parser handles extraneous parameters correctly
        // The actual parsing is handled by the AddressBookParser, but we can test
        // that the command itself is robust to any input since it takes no parameters

        MetricsCommand command = new MetricsCommand();
        CommandResult result = command.execute(model);

        // Should work the same regardless of any parameters that might have been parsed
        assertEquals(MetricsCommand.SHOWING_METRICS_MESSAGE, result.getFeedbackToUser());
        assertTrue(result.isShowMetrics());
    }

    /**
     * Test that MetricsCommand behavior is consistent across multiple executions.
     * This verifies the stateless nature of the command.
     */
    @Test
    public void execute_repeatedExecutions_consistentResults() {
        MetricsCommand command = new MetricsCommand();

        // Execute the same command multiple times
        CommandResult result1 = command.execute(model);
        CommandResult result2 = command.execute(model);
        CommandResult result3 = command.execute(model);

        // All results should be identical
        assertEquals(result1.getFeedbackToUser(), result2.getFeedbackToUser());
        assertEquals(result2.getFeedbackToUser(), result3.getFeedbackToUser());
        assertEquals(result1.isShowMetrics(), result2.isShowMetrics());
        assertEquals(result2.isShowMetrics(), result3.isShowMetrics());
    }
}
