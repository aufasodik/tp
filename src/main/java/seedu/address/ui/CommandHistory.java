package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manages the history of executed commands and provides navigation capabilities.
 * Maintains a bounded list of previously executed commands and tracks the current
 * position when navigating through history.
 */
public class CommandHistory {

    private static final int MAX_HISTORY_SIZE = 50;

    private final List<String> commandHistory;
    private int historyPointer;
    private String savedInput;

    /**
     * Creates a new CommandHistory instance with an empty history.
     */
    public CommandHistory() {
        this.commandHistory = new ArrayList<>();
        this.historyPointer = -1;
        this.savedInput = "";
    }

    /**
     * Adds a command to the command history.
     * Maintains a maximum history size by removing the oldest command if necessary.
     * Automatically resets the navigation state after adding.
     *
     * @param command The command to add to history.
     */
    public void add(String command) {
        commandHistory.add(command);

        // Remove oldest command if history exceeds maximum size
        if (commandHistory.size() > MAX_HISTORY_SIZE) {
            commandHistory.remove(0);
        }

        reset();
    }

    /**
     * Navigates to the previous command in history.
     * If currently at the user's current input, saves it before navigating.
     *
     * @param currentInput The current input text to save before navigation starts.
     * @return An Optional containing the previous command, or empty if at the beginning of history.
     */
    public Optional<String> getPrevious(String currentInput) {
        if (commandHistory.isEmpty()) {
            return Optional.empty();
        }

        // Save current input if we're starting to navigate
        if (historyPointer == -1) {
            savedInput = currentInput;
            historyPointer = commandHistory.size();
        }

        // Move to previous command if possible
        if (historyPointer > 0) {
            historyPointer--;
            return Optional.of(commandHistory.get(historyPointer));
        }

        return Optional.empty();
    }

    /**
     * Navigates to the next command in history, or returns to the saved input.
     *
     * @return An Optional containing the next command or the saved input,
     *         or empty if not currently navigating.
     */
    public Optional<String> getNext() {
        if (historyPointer == -1) {
            return Optional.empty(); // Not navigating
        }

        historyPointer++;

        if (historyPointer >= commandHistory.size()) {
            // Restore the saved input and exit navigation mode
            String inputToRestore = savedInput;
            reset();
            return Optional.of(inputToRestore);
        } else {
            // Return next command from history
            return Optional.of(commandHistory.get(historyPointer));
        }
    }

    /**
     * Resets the navigation state to the initial state (not navigating).
     * Called automatically after adding a command or completing navigation.
     */
    public void reset() {
        historyPointer = -1;
        savedInput = "";
    }

    /**
     * Returns whether the history is currently being navigated.
     *
     * @return true if currently navigating through history, false otherwise.
     */
    public boolean isNavigating() {
        return historyPointer != -1;
    }

    /**
     * Returns the current size of the command history.
     *
     * @return The number of commands stored in history.
     */
    public int size() {
        return commandHistory.size();
    }
}
