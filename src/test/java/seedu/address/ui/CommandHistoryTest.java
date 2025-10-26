package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandHistoryTest {

    private CommandHistory commandHistory;

    @BeforeEach
    public void setUp() {
        commandHistory = new CommandHistory();
    }

    @Test
    public void constructor_newCommandHistory_hasZeroSize() {
        assertEquals(0, commandHistory.size());
        assertFalse(commandHistory.isNavigating());
    }

    @Test
    public void add_singleCommand_increasesSize() {
        commandHistory.add("list");
        assertEquals(1, commandHistory.size());
    }

    @Test
    public void add_multipleCommands_increasesSize() {
        commandHistory.add("list");
        commandHistory.add("add");
        commandHistory.add("delete 1");
        assertEquals(3, commandHistory.size());
    }

    @Test
    public void add_exceedsMaxSize_removesOldestCommand() {
        // Add 51 commands (max is 50)
        for (int i = 1; i <= 51; i++) {
            commandHistory.add("command " + i);
        }
        // Size should be capped at 50
        assertEquals(50, commandHistory.size());
    }

    @Test
    public void getPrevious_emptyHistory_returnsEmpty() {
        Optional<String> result = commandHistory.getPrevious("current");
        assertFalse(result.isPresent());
    }

    @Test
    public void getPrevious_withHistory_returnsLastCommand() {
        commandHistory.add("first");
        commandHistory.add("second");

        Optional<String> result = commandHistory.getPrevious("current");
        assertTrue(result.isPresent());
        assertEquals("second", result.get());
    }

    @Test
    public void getPrevious_multipleCalls_navigatesBackwards() {
        commandHistory.add("first");
        commandHistory.add("second");
        commandHistory.add("third");

        Optional<String> first = commandHistory.getPrevious("current");
        assertEquals("third", first.get());

        Optional<String> second = commandHistory.getPrevious("current");
        assertEquals("second", second.get());

        Optional<String> third = commandHistory.getPrevious("current");
        assertEquals("first", third.get());
    }

    @Test
    public void tooMany_getPrevious_returnsEmpty() {
        commandHistory.add("first");

        commandHistory.getPrevious("current");
        Optional<String> result = commandHistory.getPrevious("current");
        assertFalse(result.isPresent());
    }

    @Test
    public void getNext_notNavigating_returnsEmpty() {
        commandHistory.add("first");

        // Down arrow key from the start
        Optional<String> result = commandHistory.getNext();
        assertFalse(result.isPresent());
    }

    @Test
    public void getNext_afterPrevious_returnsNextCommand() {
        commandHistory.add("first");
        commandHistory.add("second");

        commandHistory.getPrevious("current");
        commandHistory.getPrevious("current");

        // second -> first -> second
        Optional<String> result = commandHistory.getNext();
        assertTrue(result.isPresent());
        assertEquals("second", result.get());
    }

    @Test
    public void getNext_atEnd_returnsSavedInput() {
        commandHistory.add("first");

        commandHistory.getPrevious("my input");
        Optional<String> result = commandHistory.getNext();

        assertTrue(result.isPresent());
        assertEquals("my input", result.get());
    }

    @Test
    public void isNavigating_initialState_returnsFalse() {
        assertFalse(commandHistory.isNavigating());
    }

    @Test
    public void isNavigating_afterPrevious_returnsTrue() {
        commandHistory.add("first");
        commandHistory.getPrevious("current");

        assertTrue(commandHistory.isNavigating());
    }

    @Test
    public void isNavigating_afterReset_returnsFalse() {
        commandHistory.add("first");
        commandHistory.getPrevious("current");
        commandHistory.reset();

        assertFalse(commandHistory.isNavigating());
    }

    @Test
    public void isNavigating_afterNextToEnd_returnsFalse() {
        commandHistory.add("first");
        commandHistory.getPrevious("current");
        commandHistory.getNext();

        assertFalse(commandHistory.isNavigating());
    }

    @Test
    public void add_afterNavigation_resetsNavigationState() {
        commandHistory.add("first");
        commandHistory.getPrevious("current");

        assertTrue(commandHistory.isNavigating());

        commandHistory.add("second");
        assertFalse(commandHistory.isNavigating());
    }

    @Test
    public void getPrevious_savesDifferentInputs_restoresCorrectInput() {
        commandHistory.add("first");
        commandHistory.add("second");

        // Navigate and save "input1"
        commandHistory.getPrevious("input1");
        // Go forward to restore
        Optional<String> restored = commandHistory.getNext();

        assertEquals("input1", restored.get());

        // Navigate again with different input
        commandHistory.getPrevious("input2");
        restored = commandHistory.getNext();

        assertEquals("input2", restored.get());
    }

    @Test
    public void navigation_fullCycle_worksCorrectly() {
        commandHistory.add("cmd1");
        commandHistory.add("cmd2");
        commandHistory.add("cmd3");

        // Navigate to oldest
        assertEquals("cmd3", commandHistory.getPrevious("current").get());
        assertEquals("cmd2", commandHistory.getPrevious("current").get());
        assertEquals("cmd1", commandHistory.getPrevious("current").get());

        // Try to go further back
        assertFalse(commandHistory.getPrevious("current").isPresent());

        // Navigate forward
        assertEquals("cmd2", commandHistory.getNext().get());
        assertEquals("cmd3", commandHistory.getNext().get());
        assertEquals("current", commandHistory.getNext().get());

        // Should no longer be navigating
        assertFalse(commandHistory.isNavigating());
    }
}
