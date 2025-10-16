package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_COMPANIES_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCompanies.CONS;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Status;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FilterCommand byApplied = new FilterCommand(new Status("applied"));
        FilterCommand byOa = new FilterCommand(new Status("oa"));

        // same object -> returns true
        assertTrue(byApplied.equals(byApplied));

        // same values -> returns true
        FilterCommand byAppliedCopy = new FilterCommand(new Status("applied"));
        assertTrue(byApplied.equals(byAppliedCopy));

        // different types -> returns false
        assertFalse(byApplied.equals(1));

        // null -> returns false
        assertFalse(byApplied.equals(null));

        // different status -> returns false
        assertFalse(byApplied.equals(byOa));
    }

    @Test
    public void execute_matchingStatus_singleCompanyFound() {
        String expectedMessage = String.format(MESSAGE_COMPANIES_LISTED_OVERVIEW, 1);
        FilterCommand command = new FilterCommand(new Status("applied"));
        expectedModel.updateFilteredCompanyList(c -> c.getStatus().equals(new Status("applied")));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CONS), model.getFilteredCompanyList());
    }

    @Test
    public void execute_noCompanyWithStatus_zeroFound() {
        String expectedMessage = String.format(MESSAGE_COMPANIES_LISTED_OVERVIEW, 0);
        FilterCommand command = new FilterCommand(new Status("accepted"));
        expectedModel.updateFilteredCompanyList(c -> c.getStatus().equals(new Status("accepted")));
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredCompanyList());
    }

    @Test
    public void toStringMethod() {
        Status status = new Status("oa");
        FilterCommand command = new FilterCommand(status);
        String expected = FilterCommand.class.getCanonicalName() + "{status=" + status + "}";
        assertEquals(expected, command.toString());
    }
}

