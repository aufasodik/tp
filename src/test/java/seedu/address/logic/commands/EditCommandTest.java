package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showcompanyAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_company;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_company;
import static seedu.address.testutil.TypicalCompanies.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditcompanyDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.company.Company;
import seedu.address.testutil.EditCompanyDescriptorBuilder;
import seedu.address.testutil.CompanyBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Company editedCompany = new CompanyBuilder().build();
        EditcompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(editedCompany).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_company, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_company_SUCCESS, Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setcompany(model.getFilteredcompanyList().get(0), editedCompany);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastcompany = Index.fromOneBased(model.getFilteredcompanyList().size());
        Company lastCompany = model.getFilteredcompanyList().get(indexLastcompany.getZeroBased());

        CompanyBuilder companyInList = new CompanyBuilder(lastCompany);
        Company editedCompany = companyInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withTags(VALID_TAG_HUSBAND).build();

        EditcompanyDescriptor descriptor = new EditCompanyDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastcompany, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_company_SUCCESS, Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setcompany(lastCompany, editedCompany);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_company, new EditcompanyDescriptor());
        Company editedCompany = model.getFilteredcompanyList().get(INDEX_FIRST_company.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_company_SUCCESS, Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showcompanyAtIndex(model, INDEX_FIRST_company);

        Company companyInFilteredList = model.getFilteredcompanyList().get(INDEX_FIRST_company.getZeroBased());
        Company editedCompany = new CompanyBuilder(companyInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_company,
                new EditCompanyDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_company_SUCCESS, Messages.format(editedCompany));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setcompany(model.getFilteredcompanyList().get(0), editedCompany);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatecompanyUnfilteredList_failure() {
        Company firstCompany = model.getFilteredcompanyList().get(INDEX_FIRST_company.getZeroBased());
        EditcompanyDescriptor descriptor = new EditCompanyDescriptorBuilder(firstCompany).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_company, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_company);
    }

    @Test
    public void execute_duplicatecompanyFilteredList_failure() {
        showcompanyAtIndex(model, INDEX_FIRST_company);

        // edit company in filtered list into a duplicate in address book
        Company companyInList = model.getAddressBook().getCompanyList().get(INDEX_SECOND_company.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_company,
                new EditCompanyDescriptorBuilder(companyInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_company);
    }

    @Test
    public void execute_invalidcompanyIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredcompanyList().size() + 1);
        EditcompanyDescriptor descriptor = new EditCompanyDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_company_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidcompanyIndexFilteredList_failure() {
        showcompanyAtIndex(model, INDEX_FIRST_company);
        Index outOfBoundIndex = INDEX_SECOND_company;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getCompanyList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditCompanyDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_company_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_company, DESC_AMY);

        // same values -> returns true
        EditcompanyDescriptor copyDescriptor = new EditcompanyDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_company, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_company, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_company, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditcompanyDescriptor editcompanyDescriptor = new EditcompanyDescriptor();
        EditCommand editCommand = new EditCommand(index, editcompanyDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editcompanyDescriptor="
                + editcompanyDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
