package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalCompanies.ALPHA;
import static seedu.address.testutil.TypicalCompanies.BETA;
import static seedu.address.testutil.TypicalCompanies.CONS;
import static seedu.address.testutil.TypicalCompanies.DELTA;
import static seedu.address.testutil.TypicalCompanies.HOLLY;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.FilterPredicate;
import seedu.address.model.company.Status;

/**
 * Contains integration tests (interaction with the Model) for {@code FilterCommand}.
 */
public class FilterCommandTest {
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    // Verifies equals method works correctly for FilterCommand objects
    @Test
    public void equals() {
        FilterCommand byApplied = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        FilterCommand byOa = new FilterCommand(
                Optional.of(new Status("oa")), Collections.emptyList());
        FilterCommand byTagJava = new FilterCommand(
                Optional.empty(), Collections.singletonList("java"));

        // same object -> returns true
        assertTrue(byApplied.equals(byApplied));

        // same values -> returns true
        FilterCommand byAppliedCopy = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        assertTrue(byApplied.equals(byAppliedCopy));

        // different types -> returns false
        assertFalse(byApplied.equals(1));
        assertFalse(byApplied.equals("applied"));

        // null -> returns false
        assertFalse(byApplied.equals(null));

        // different status -> returns false
        assertFalse(byApplied.equals(byOa));

        // different tags -> returns false
        assertFalse(byApplied.equals(byTagJava));

        // case insensitive status -> returns true
        FilterCommand byAppliedUpperCase = new FilterCommand(
                Optional.of(new Status("APPLIED")), Collections.emptyList());
        assertTrue(byApplied.equals(byAppliedUpperCase));

        // same status and tags -> returns true
        FilterCommand combined1 = new FilterCommand(
                Optional.of(new Status("applied")), Collections.singletonList("java"));
        FilterCommand combined2 = new FilterCommand(
                Optional.of(new Status("applied")), Collections.singletonList("java"));
        assertTrue(combined1.equals(combined2));
    }

    // Ensures filtering finds the correct company with given status (1 company found)
    @Test
    public void execute_matchingStatus_singleCompanyFound() {
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("applied")), Collections.emptyList()));
        String expectedMessage = "1 companies listed!\nFiltered by status: applied";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CONS), model.getFilteredCompanyList());
    }

    // Ensures filtering finds the correct company with given status (multiple companies found)
    @Test
    public void execute_matchingStatus_multipleCompaniesFound() {
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("to-apply")), Collections.emptyList());
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("to-apply")), Collections.emptyList()));
        String expectedMessage = "2 companies listed!\nFiltered by status: to-apply";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALPHA, HOLLY), model.getFilteredCompanyList());
    }

    // Ensures filtering correctly yields empty list when no matches.
    @Test
    public void execute_noCompanyWithStatus_zeroFound() {
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("accepted")), Collections.emptyList());
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("accepted")), Collections.emptyList()));
        String expectedMessage = "0 companies listed!\nFiltered by status: accepted";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredCompanyList());
    }

    // Verifies filtering doesn't mutate the model's address book contents.
    @Test
    public void execute_filterDoesNotModifyOriginalList() {
        AddressBook original = new AddressBook(model.getAddressBook());
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        command.execute(model);
        assertEquals(original, model.getAddressBook());
    }

    // Checks toString() returns the correct string.
    @Test
    public void toStringMethod() {
        Optional<Status> status = Optional.of(new Status("oa"));
        FilterCommand command = new FilterCommand(status, Collections.emptyList());
        String expected = FilterCommand.class.getCanonicalName()
                + "{status=" + status + ", tagKeywords=[]}";
        assertEquals(expected, command.toString());
    }

    // Ensures constructor rejects null status parameter
    @Test
    public void constructor_nullStatus_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterCommand(null, Collections.emptyList()));
    }

    // Ensures constructor rejects null tagKeywords parameter
    @Test
    public void constructor_nullTagKeywords_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new FilterCommand(Optional.of(new Status("applied")), null));
    }

    // Ensures execute method rejects null model parameter
    @Test
    public void execute_nullModel_throwsNullPointerException() {
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    // Tests filtering on empty address book returns no companies
    @Test
    public void execute_emptyAddressBook_zeroFound() {
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        Model expectedEmptyModel = new ModelManager(new AddressBook(), new UserPrefs());

        FilterCommand command = new FilterCommand(
                Optional.of(new Status("applied")), Collections.emptyList());
        expectedEmptyModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("applied")), Collections.emptyList()));
        String expectedMessage = "0 companies listed!\nFiltered by status: applied";
        assertCommandSuccess(command, emptyModel, expectedMessage, expectedEmptyModel);
        assertEquals(Collections.emptyList(), emptyModel.getFilteredCompanyList());
    }

    // Verifies that filtering is case-insensitive.
    @Test
    public void execute_caseInsensitiveStatusMatch_success() {
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("APPLIED")), Collections.emptyList()); // uppercase
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("applied")), Collections.emptyList()));
        String expectedMessage = "1 companies listed!\nFiltered by status: applied";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // Tests filtering by single tag keyword
    @Test
    public void execute_matchingTag_companiesFound() {
        // Filter by "supplier" tag - should find ALPHA and DELTA
        FilterCommand command = new FilterCommand(
                Optional.empty(), Collections.singletonList("supplier"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Collections.singletonList("supplier")));
        String expectedMessage = "2 companies listed!\nFiltered by tags containing: [supplier]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALPHA, DELTA), model.getFilteredCompanyList());
    }

    // Tests filtering by tag substring matching
    @Test
    public void execute_tagSubstringMatching_companiesFound() {
        // Filter by "cli" - should match "client" tag in BETA
        FilterCommand command = new FilterCommand(
                Optional.empty(), Collections.singletonList("cli"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Collections.singletonList("cli")));
        String expectedMessage = "1 companies listed!\nFiltered by tags containing: [cli]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BETA), model.getFilteredCompanyList());
    }

    // Tests filtering by multiple tags (OR logic)
    @Test
    public void execute_multipleTags_companiesWithAnyTagFound() {
        // Filter by "client" OR "partner" - should find BETA (has both)
        FilterCommand command = new FilterCommand(
                Optional.empty(), Arrays.asList("client", "partner"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Arrays.asList("client", "partner")));
        String expectedMessage = "1 companies listed!\nFiltered by tags containing: [client, partner]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BETA), model.getFilteredCompanyList());
    }

    // Tests filtering by multiple tags where different companies match different tags (OR logic)
    @Test
    public void execute_multipleTagsOrLogic_multipleCompaniesFound() {
        // Filter by "supplier" OR "client" - ALPHA and DELTA have supplier, BETA has client
        FilterCommand command = new FilterCommand(
                Optional.empty(), Arrays.asList("supplier", "client"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Arrays.asList("supplier", "client")));
        String expectedMessage = "3 companies listed!\nFiltered by tags containing: [supplier, client]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALPHA, BETA, DELTA), model.getFilteredCompanyList());
    }

    // Tests filtering by tag that doesn't match any company
    @Test
    public void execute_noMatchingTag_zeroFound() {
        FilterCommand command = new FilterCommand(
                Optional.empty(), Collections.singletonList("python"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Collections.singletonList("python")));
        String expectedMessage = "0 companies listed!\nFiltered by tags containing: [python]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredCompanyList());
    }

    // Tests combined status and tag filtering
    @Test
    public void execute_statusAndTag_companiesFound() {
        // Filter by status "tech-interview" AND tag "client" - should find BETA
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("tech-interview")), Collections.singletonList("client"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("tech-interview")),
                        Collections.singletonList("client")));
        String expectedMessage = "1 companies listed!\n"
                + "Filtered by status: tech-interview and tags containing: [client]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(BETA), model.getFilteredCompanyList());
    }

    // Tests combined filtering with no matches
    @Test
    public void execute_statusAndTagNoMatch_zeroFound() {
        // Filter by status "applied" AND tag "supplier" - CONS has applied but no supplier tag
        FilterCommand command = new FilterCommand(
                Optional.of(new Status("applied")), Collections.singletonList("supplier"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.of(new Status("applied")),
                        Collections.singletonList("supplier")));
        String expectedMessage = "0 companies listed!\nFiltered by status: applied and tags containing: [supplier]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredCompanyList());
    }

    // Tests case-insensitive tag matching
    @Test
    public void execute_caseInsensitiveTagMatch_success() {
        // Filter by "SUPPLIER" (uppercase) - should match "supplier" tag
        FilterCommand command = new FilterCommand(
                Optional.empty(), Collections.singletonList("SUPPLIER"));
        expectedModel.updateFilteredCompanyList(
                new FilterPredicate(Optional.empty(), Collections.singletonList("SUPPLIER")));
        String expectedMessage = "2 companies listed!\nFiltered by tags containing: [SUPPLIER]";
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALPHA, DELTA), model.getFilteredCompanyList());
    }
}
