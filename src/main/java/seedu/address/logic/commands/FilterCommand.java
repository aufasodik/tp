package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
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

    public static final String MESSAGE_COMPANIES_LISTED_OVERVIEW = "%1$d %2$s listed!";
    public static final String MESSAGE_FILTER_STATUS = "\nFiltered by status: %1$s";
    public static final String MESSAGE_FILTER_TAGS = "\nFiltered by tags containing: %1$s";
    public static final String MESSAGE_FILTER_STATUS_AND_TAGS = "\nFiltered by status: %1$s and tags containing: %2$s";

    private final Logger logger = LogsCenter.getLogger(getClass());

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
        logger.info("Executing filter: status=%s, tags=%s".formatted(status, tagKeywords));
        FilterPredicate predicate = new FilterPredicate(status, tagKeywords);
        model.updateFilteredCompanyList(predicate);

        return new CommandResult(generateSuccessMessage(model.getFilteredCompanyList().size()));
    }

    /**
     * Generates a success message based on the filters applied and the number of companies found.
     */
    private String generateSuccessMessage(int count) {
        assert(count >= 0) : "Count must be non-negative.";

        StringBuilder message = new StringBuilder();
        String countWord = (count == 1) ? "company" : "companies";
        message.append(String.format(MESSAGE_COMPANIES_LISTED_OVERVIEW, count, countWord));

        if (status.isPresent() && !tagKeywords.isEmpty()) {
            message.append(String.format(MESSAGE_FILTER_STATUS_AND_TAGS, status.get(), tagKeywords));
        } else if (status.isPresent()) {
            message.append(String.format(MESSAGE_FILTER_STATUS, status.get()));
        } else if (!tagKeywords.isEmpty()) {
            message.append(String.format(MESSAGE_FILTER_TAGS, tagKeywords));
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

