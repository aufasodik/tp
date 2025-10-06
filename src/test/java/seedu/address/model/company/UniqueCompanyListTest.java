package seedu.address.model.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCompanies.ALICE;
import static seedu.address.testutil.TypicalCompanies.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.company.exceptions.DuplicateCompanyException;
import seedu.address.model.company.exceptions.CompanyNotFoundException;
import seedu.address.testutil.CompanyBuilder;

public class UniqueCompanyListTest {

    private final UniqueCompanyList uniqueCompanyList = new UniqueCompanyList();

    @Test
    public void contains_nullcompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.contains(null));
    }

    @Test
    public void contains_companyNotInList_returnsFalse() {
        assertFalse(uniqueCompanyList.contains(ALICE));
    }

    @Test
    public void contains_companyInList_returnsTrue() {
        uniqueCompanyList.add(ALICE);
        assertTrue(uniqueCompanyList.contains(ALICE));
    }

    @Test
    public void contains_companyWithSameIdentityFieldsInList_returnsTrue() {
        uniqueCompanyList.add(ALICE);
        Company editedAlice = new CompanyBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueCompanyList.contains(editedAlice));
    }

    @Test
    public void add_nullcompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.add(null));
    }

    @Test
    public void add_duplicatecompany_throwsDuplicatecompanyException() {
        uniqueCompanyList.add(ALICE);
        assertThrows(DuplicateCompanyException.class, () -> uniqueCompanyList.add(ALICE));
    }

    @Test
    public void setcompany_nullTargetcompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.setcompany(null, ALICE));
    }

    @Test
    public void setcompany_nullEditedcompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.setcompany(ALICE, null));
    }

    @Test
    public void setcompany_targetcompanyNotInList_throwscompanyNotFoundException() {
        assertThrows(CompanyNotFoundException.class, () -> uniqueCompanyList.setcompany(ALICE, ALICE));
    }

    @Test
    public void setcompany_editedcompanyIsSamecompany_success() {
        uniqueCompanyList.add(ALICE);
        uniqueCompanyList.setcompany(ALICE, ALICE);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        expectedUniqueCompanyList.add(ALICE);
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setcompany_editedcompanyHasSameIdentity_success() {
        uniqueCompanyList.add(ALICE);
        Company editedAlice = new CompanyBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueCompanyList.setcompany(ALICE, editedAlice);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        expectedUniqueCompanyList.add(editedAlice);
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setcompany_editedcompanyHasDifferentIdentity_success() {
        uniqueCompanyList.add(ALICE);
        uniqueCompanyList.setcompany(ALICE, BOB);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        expectedUniqueCompanyList.add(BOB);
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setcompany_editedcompanyHasNonUniqueIdentity_throwsDuplicatecompanyException() {
        uniqueCompanyList.add(ALICE);
        uniqueCompanyList.add(BOB);
        assertThrows(DuplicateCompanyException.class, () -> uniqueCompanyList.setcompany(ALICE, BOB));
    }

    @Test
    public void remove_nullcompany_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.remove(null));
    }

    @Test
    public void remove_companyDoesNotExist_throwscompanyNotFoundException() {
        assertThrows(CompanyNotFoundException.class, () -> uniqueCompanyList.remove(ALICE));
    }

    @Test
    public void remove_existingcompany_removescompany() {
        uniqueCompanyList.add(ALICE);
        uniqueCompanyList.remove(ALICE);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setCompanies_nullUniquecompanyList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.setCompanies((UniqueCompanyList) null));
    }

    @Test
    public void setCompanies_uniquecompanyList_replacesOwnListWithProvidedUniquecompanyList() {
        uniqueCompanyList.add(ALICE);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        expectedUniqueCompanyList.add(BOB);
        uniqueCompanyList.setCompanies(expectedUniqueCompanyList);
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setCompanies_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueCompanyList.setCompanies((List<Company>) null));
    }

    @Test
    public void setCompanies_list_replacesOwnListWithProvidedList() {
        uniqueCompanyList.add(ALICE);
        List<Company> companyList = Collections.singletonList(BOB);
        uniqueCompanyList.setCompanies(companyList);
        UniqueCompanyList expectedUniqueCompanyList = new UniqueCompanyList();
        expectedUniqueCompanyList.add(BOB);
        assertEquals(expectedUniqueCompanyList, uniqueCompanyList);
    }

    @Test
    public void setcompanies_listWithDuplicateCompanies_throwsDuplicatecompanyException() {
        List<Company> listWithDuplicateCompanies = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateCompanyException.class, () -> uniqueCompanyList.setCompanies(listWithDuplicateCompanies));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueCompanyList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueCompanyList.asUnmodifiableObservableList().toString(), uniqueCompanyList.toString());
    }
}
