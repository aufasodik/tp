package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.company.Company;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND =
                "Unknown command! Use `help` to view a summary of commands available";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_COMPANY_DISPLAYED_INDEX = "The company index provided is invalid";
    public static final String MESSAGE_COMPANIES_LISTED_OVERVIEW = "%1$d companies listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code company} for display to the user.
     */
    public static String format(Company company) {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder tagBuilder = new StringBuilder();

        // Format phone with user-friendly placeholder
        String phoneDisplay = company.getPhone().value == null
                ? "None"
                : company.getPhone().value;

        // Format email with user-friendly placeholder
        String emailDisplay = company.getEmail().value == null
                ? "None"
                : company.getEmail().value;

        // Format address with user-friendly placeholder
        String addressDisplay = company.getAddress().value == null
                ? "None"
                : company.getAddress().value;

        // Format tags with user-friendly placeholder
        if (company.getTags().isEmpty()) {
            tagBuilder.append("None");
        } else {
            company.getTags().forEach(tagBuilder::append);
        }
        String tagDisplay = tagBuilder.toString();

        // Format remarks with user-friendly placeholder
        String remarkDisplay = company.getRemark().value == null
                ? "None"
                : company.getRemark().value;

        builder.append(company.getName())
                .append("; Phone: ")
                .append(phoneDisplay)
                .append("; Email: ")
                .append(emailDisplay)
                .append("; Address: ")
                .append(addressDisplay)
                .append("; Status: ")
                .append(company.getStatus())
                .append("; Tags: ")
                .append(tagDisplay)
                .append("; Remarks: ")
                .append(remarkDisplay);
        return builder.toString();
    }
}
