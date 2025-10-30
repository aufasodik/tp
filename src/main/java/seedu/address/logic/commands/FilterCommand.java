package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.company.FilterPredicate;
import seedu.address.model.company.Status;

/**
 * Filters and lists all companies that match the given status and/or tags.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters companies by application status "
            + "and/or tags, then displays them as a list with index numbers.\n"
            + "At least one filter criterion must be provided.\n"
            + "Parameters: [s/STATUS] [t/TAG_KEYWORD]...\n"
            + "Status values: to-apply, applied, oa, tech-interview, hr-interview, in-process, "
            + "offered, accepted, rejected\n"
            + "Examples:\n"
            + "- " + COMMAND_WORD + " s/applied\n"
            + "- " + COMMAND_WORD + " t/java t/remote\n"
            + "- " + COMMAND_WORD + " s/offered t/good";

    private final Optional<Status> status;
    private final List<String> tagKeywords;

    /**
     * Creates a FilterCommand object with the given status and tag keywords.
     *
     * @param status Optional status to filter by
     * @param tagKeywords List of tag keywords to filter by (can be empty)
     */
    public FilterCommand(Optional<Status> status, List<String> tagKeywords) {
        requireNonNull(status);
        requireNonNull(tagKeywords);
        this.status = status;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        FilterPredicate predicate = new FilterPredicate(status, tagKeywords);
        model.updateFilteredCompanyList(predicate);

        return new CommandResult(generateSuccessMessage(model.getFilteredCompanyList().size()));
    }

    /**
     * Generates a success message based on the filters applied and the number of companies found.
     */
    private String generateSuccessMessage(int count) {
        StringBuilder message = new StringBuilder();
        message.append(String.format("%d companies listed!", count));

        if (status.isPresent() && !tagKeywords.isEmpty()) {
            message.append(String.format("\nFiltered by status: %s and tags containing: %s",
                    status.get(), tagKeywords));
        } else if (status.isPresent()) {
            message.append(String.format("\nFiltered by status: %s", status.get()));
        } else if (!tagKeywords.isEmpty()) {
            message.append(String.format("\nFiltered by tags containing: %s", tagKeywords));
        }

        return message.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FilterCommand)) {
            return false;
        }
        FilterCommand otherCommand = (FilterCommand) other;
        return status.equals(otherCommand.status)
                && tagKeywords.equals(otherCommand.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("status", status)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}

