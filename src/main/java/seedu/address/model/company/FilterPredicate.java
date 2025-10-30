package seedu.address.model.company;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Company} matches the given status and/or tag filters.
 * For tags, at least one keyword must match at least one tag (OR logic between keywords).
 * Tag matching is case-insensitive and uses substring matching.
 */
public class FilterPredicate implements Predicate<Company> {
    private final Optional<Status> status;
    private final List<String> tagKeywords;

    /**
     * Creates a FilterPredicate with the given status and tag keywords.
     *
     * @param status Optional status to filter by
     * @param tagKeywords List of tag keywords to filter by (can be empty)
     */
    public FilterPredicate(Optional<Status> status, List<String> tagKeywords) {
        this.status = status;
        this.tagKeywords = tagKeywords;
    }

    @Override
    public boolean test(Company company) {
        boolean statusMatch = status.isEmpty() || company.getStatus().equals(status.get());

        boolean tagsMatch = tagKeywords.isEmpty() || tagKeywords.stream()
                .anyMatch(keyword -> company.getTags().stream()
                        .anyMatch(tag -> tag.tagName.toLowerCase()
                                .contains(keyword.toLowerCase())));

        return statusMatch && tagsMatch;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterPredicate)) {
            return false;
        }

        FilterPredicate otherFilterPredicate = (FilterPredicate) other;
        return status.equals(otherFilterPredicate.status)
                && tagKeywords.equals(otherFilterPredicate.tagKeywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("status", status)
                .add("tagKeywords", tagKeywords)
                .toString();
    }
}
