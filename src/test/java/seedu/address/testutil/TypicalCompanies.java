package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.company.Company;

/**
 * A utility class containing a list of {@code Company} objects to be used in tests.
 */
public class TypicalCompanies {

    public static final Company ALICE = new CompanyBuilder().withName("Alpha Industries")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("contact@alpha.com")
            .withPhone("94351253")
            .withTags("supplier").build();
    public static final Company BENSON = new CompanyBuilder().withName("Beta Corporation")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("info@beta.com").withPhone("98765432")
            .withTags("client", "partner").build();
    public static final Company CARL = new CompanyBuilder().withName("Consolidated Traders").withPhone("95352563")
            .withEmail("sales@consolidated.com").withAddress("1 Wall Street Plaza").build();
    public static final Company DANIEL = new CompanyBuilder().withName("Delta Enterprises").withPhone("87652533")
            .withEmail("contact@delta.com").withAddress("10 Enterprise Road").withTags("supplier").build();
    public static final Company ELLE = new CompanyBuilder().withName("Elite Manufacturing").withPhone("9482224")
            .withEmail("enquiry@elite.com").withAddress("50 Michigan Avenue").build();
    public static final Company FIONA = new CompanyBuilder().withName("Fusion Systems").withPhone("9482427")
            .withEmail("hello@fusion.com").withAddress("88 Little Tokyo Street").build();
    public static final Company GEORGE = new CompanyBuilder().withName("Global Trading Co").withPhone("9482442")
            .withEmail("info@globaltrading.com").withAddress("4 Commerce Street").build();

    // Manually added
    public static final Company HOON = new CompanyBuilder().withName("Horizon Logistics").withPhone("8482424")
            .withEmail("contact@horizon.com").withAddress("15 Little India Road").build();
    public static final Company IDA = new CompanyBuilder().withName("Innovative Solutions").withPhone("8482131")
            .withEmail("info@innovative.com").withAddress("72 Chicago Avenue").build();

    // Manually added - Company's details found in {@code CommandTestUtil}
    public static final Company AMY = new CompanyBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Company BOB = new CompanyBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Corporation"; // A keyword that matches Corporation

    private TypicalCompanies() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical companies.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Company company : getTypicalcompanies()) {
            ab.addCompany(company);
        }
        return ab;
    }

    public static List<Company> getTypicalcompanies() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
