package seedu.address.model.company;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Company}'s {@code Name} contains any of the keywords given as substrings.
 * Keyword matching is case insensitive.
 */
public class NameContainsKeywordsPredicate implements Predicate<Company> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Company company) {
        return keywords.stream()
                .anyMatch(keyword -> company.getName().fullName.toLowerCase()
                        .contains(keyword.toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate)) {
            return false;
        }

        NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate = (NameContainsKeywordsPredicate) other;
        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public int hashCode() {
        return Objects.hash(keywords); // List hash is content-based
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
