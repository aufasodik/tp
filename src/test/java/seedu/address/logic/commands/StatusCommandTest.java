package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_COMPANY;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_COMPANY;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Company;
import seedu.address.model.company.exceptions.UnsupportedStatusException;
import seedu.address.model.company.Status;
import seedu.address.testutil.CompanyBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for StatusCommand.
 */
public class StatusCommandTest {

    // Use the hyphenated form to match user-facing MESSAGE_USAGE and typical CLI convention
    private static final String STATUS_STUB = "hr_interview";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    /**
     * Verifies that a valid status update succeeds and the filtered list is reset
     * to show all companies (since the command calls updateFilteredCompanyList with the "show all" predicate).
     */
    @Test
    public void execute_statusAcceptedByModel_updateSuccessful() throws Exception {
        Company firstCompany = model.getFilteredCompanyList().get(INDEX_FIRST_COMPANY.getZeroBased());
        Company editedCompany = new CompanyBuilder(firstCompany).withStatus(STATUS_STUB).build();

        StatusCommand statusCommand = new StatusCommand(INDEX_FIRST_COMPANY, new Status(STATUS_STUB));

        String expectedMessage = String.format(StatusCommand.MESSAGE_UPDATE_STATUS_SUCCESS,
                Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setCompany(firstCompany, editedCompany);

        assertCommandSuccess(statusCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Ensures an out-of-bounds index on the unfiltered list is rejected.
     */
    @Test
    public void execute_invalidCompanyIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredCompanyList().size() + 1);
        StatusCommand statusCommand = new StatusCommand(outOfBoundIndex, new Status(STATUS_STUB));

        assertCommandFailure(statusCommand, model, Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    /**
     * Ensures an out-of-bounds index is rejected when the list is filtered.
     * This covers the common pitfall where code uses the wrong backing list for index checks.
     */
    @Test
    public void execute_invalidCompanyIndexFilteredList_failure() {
        // Filter down to only the first company
        Company firstCompany = model.getFilteredCompanyList().get(INDEX_FIRST_COMPANY.getZeroBased());
        model.updateFilteredCompanyList(company -> company.equals(firstCompany));

        // INDEX_SECOND_COMPANY is now out of bounds for the filtered list
        StatusCommand statusCommand = new StatusCommand(INDEX_SECOND_COMPANY, new Status(STATUS_STUB));

        assertCommandFailure(statusCommand, model, Messages.MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX);
    }

    /**
     * Verifies that setting the same status as the current one still produces a successful
     * result and stable model state (idempotent behavior).
     */
    @Test
    public void execute_sameStatusNoChange_success() throws Exception {
        Company target = model.getFilteredCompanyList().get(INDEX_FIRST_COMPANY.getZeroBased());

        // Reuse the existing Status object instead of converting to String
        Status sameStatus = target.getStatus();

        StatusCommand statusCommand = new StatusCommand(INDEX_FIRST_COMPANY, sameStatus);

        // Build an identical company (no change)
        Company editedCompany = new CompanyBuilder(target).build();

        String expectedMessage = String.format(StatusCommand.MESSAGE_UPDATE_STATUS_SUCCESS,
                Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setCompany(target, editedCompany);
        expectedModel.updateFilteredCompanyList(Model.PREDICATE_SHOW_ALL_COMPANIES);

        assertCommandSuccess(statusCommand, model, expectedMessage, expectedModel);
    }

    /**
     * Constructor should reject a null index to prevent NPEs later (requireAllNonNull).
     */
    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StatusCommand(null, new Status(STATUS_STUB)));
    }

    /**
     * Constructor should reject a null status to prevent NPEs later (requireAllNonNull).
     */
    @Test
    public void constructor_nullStatus_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StatusCommand(INDEX_FIRST_COMPANY, null));
    }

    /**
     * Validates that constructing a Status with an unknown label fails fast.
     * This protects the domain invariant (allowed status set only) and guards
     * against regressions if validation is weakened in the future.
     */
    @Test
    public void status_invalidValue_throwsUnsupportedStatusException() {
        String invalid = "NOT_A_VALID_STATUS";
        assertThrows(UnsupportedStatusException.class, () -> new Status(invalid));
    }

    @Test
    public void equals() {
        final StatusCommand standardCommand = new StatusCommand(INDEX_FIRST_COMPANY,
                new Status("to-apply"));

        // same values -> returns true
        StatusCommand commandWithSameValues = new StatusCommand(INDEX_FIRST_COMPANY,
                new Status("to-apply"));
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new StatusCommand(INDEX_SECOND_COMPANY,
                new Status("to-apply"))));

        // different status -> returns false
        assertFalse(standardCommand.equals(new StatusCommand(INDEX_FIRST_COMPANY,
                new Status("in-process"))));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        Status status = new Status("to-apply");
        StatusCommand statusCommand = new StatusCommand(index, status);
        String expected = StatusCommand.class.getCanonicalName()
                + "{index=" + index + ", status=" + status + "}";
        assertEquals(expected, statusCommand.toString());
    }
}
