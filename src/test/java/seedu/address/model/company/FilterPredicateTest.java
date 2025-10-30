package seedu.address.model.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.CompanyBuilder;

public class FilterPredicateTest {

    @Test
    public void equals() {
        Optional<Status> firstStatus = Optional.of(new Status("applied"));
        Optional<Status> secondStatus = Optional.of(new Status("offered"));
        List<String> firstTagList = Collections.singletonList("java");
        List<String> secondTagList = Arrays.asList("java", "remote");

        FilterPredicate firstPredicate = new FilterPredicate(firstStatus, firstTagList);
        FilterPredicate secondPredicate = new FilterPredicate(secondStatus, secondTagList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        FilterPredicate firstPredicateCopy = new FilterPredicate(firstStatus, firstTagList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different status -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));

        // different tag keywords -> returns false
        FilterPredicate thirdPredicate = new FilterPredicate(firstStatus, secondTagList);
        assertFalse(firstPredicate.equals(thirdPredicate));
    }

    @Test
    public void test_statusOnlyFilter_returnsTrue() {
        // Status filter only - exact match
        FilterPredicate predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Collections.emptyList());
        assertTrue(predicate.test(new CompanyBuilder().withStatus("applied").build()));

        // Different status format
        predicate = new FilterPredicate(
                Optional.of(new Status("tech-interview")), Collections.emptyList());
        assertTrue(predicate.test(new CompanyBuilder().withStatus("tech-interview").build()));
    }

    @Test
    public void test_statusOnlyFilter_returnsFalse() {
        // Status doesn't match
        FilterPredicate predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Collections.emptyList());
        assertFalse(predicate.test(new CompanyBuilder().withStatus("offered").build()));
    }

    @Test
    public void test_tagOnlySingleKeyword_returnsTrue() {
        // Single tag keyword - exact match
        FilterPredicate predicate = new FilterPredicate(
                Optional.empty(), Collections.singletonList("java"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("java").build()));

        // Single tag keyword - substring match (beginning)
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("rem"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("remote-work").build()));

        // Single tag keyword - substring match (middle)
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("mot"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("remote-work").build()));

        // Single tag keyword - substring match (end)
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("work"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("remote-work").build()));

        // Case insensitive matching
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("JAVA"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("java").build()));

        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("java"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("JAVA").build()));

        // Matches one of multiple tags
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("java"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("python", "java", "cpp").build()));
    }

    @Test
    public void test_tagOnlyMultipleKeywords_returnsTrue() {
        // Multiple tag keywords - all match (AND logic)
        FilterPredicate predicate = new FilterPredicate(
                Optional.empty(), Arrays.asList("java", "remote"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("java", "remote-work").build()));

        // Multiple keywords match different tags
        predicate = new FilterPredicate(Optional.empty(), Arrays.asList("good", "rem"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("good-pay", "remote-work", "python").build()));

        // Multiple keywords with case insensitive matching
        predicate = new FilterPredicate(Optional.empty(), Arrays.asList("JAVA", "REM"));
        assertTrue(predicate.test(new CompanyBuilder().withTags("java", "remote-work").build()));
    }

    @Test
    public void test_tagOnlyFilter_returnsFalse() {
        // No tags in company
        FilterPredicate predicate = new FilterPredicate(
                Optional.empty(), Collections.singletonList("java"));
        assertFalse(predicate.test(new CompanyBuilder().withTags().build()));

        // Tag keyword doesn't match any tag
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("python"));
        assertFalse(predicate.test(new CompanyBuilder().withTags("java", "cpp").build()));

        // Multiple keywords - not all match (AND logic fails)
        predicate = new FilterPredicate(Optional.empty(), Arrays.asList("java", "python"));
        assertFalse(predicate.test(new CompanyBuilder().withTags("java", "cpp").build()));

        // Substring not found
        predicate = new FilterPredicate(Optional.empty(), Collections.singletonList("xyz"));
        assertFalse(predicate.test(new CompanyBuilder().withTags("java", "remote-work").build()));
    }

    @Test
    public void test_combinedStatusAndTagFilter_returnsTrue() {
        // Both status and tag match
        FilterPredicate predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Collections.singletonList("java"));
        assertTrue(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java")
                .build()));

        // Status and multiple tags match
        predicate = new FilterPredicate(
                Optional.of(new Status("offered")), Arrays.asList("java", "remote"));
        assertTrue(predicate.test(new CompanyBuilder()
                .withStatus("offered")
                .withTags("java", "remote-work", "good-pay")
                .build()));

        // Case insensitive combined filtering
        predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Collections.singletonList("JAVA"));
        assertTrue(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java")
                .build()));
    }

    @Test
    public void test_combinedStatusAndTagFilter_returnsFalse() {
        // Status matches but tag doesn't
        FilterPredicate predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Collections.singletonList("python"));
        assertFalse(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java")
                .build()));

        // Tag matches but status doesn't
        predicate = new FilterPredicate(
                Optional.of(new Status("offered")), Collections.singletonList("java"));
        assertFalse(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java")
                .build()));

        // Neither status nor tag matches
        predicate = new FilterPredicate(
                Optional.of(new Status("offered")), Collections.singletonList("python"));
        assertFalse(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java")
                .build()));

        // Status matches but not all tags match
        predicate = new FilterPredicate(
                Optional.of(new Status("applied")), Arrays.asList("java", "python"));
        assertFalse(predicate.test(new CompanyBuilder()
                .withStatus("applied")
                .withTags("java", "cpp")
                .build()));
    }

    @Test
    public void test_emptyFilters_returnsTrue() {
        // No status and no tag keywords - matches everything
        FilterPredicate predicate = new FilterPredicate(Optional.empty(), Collections.emptyList());
        assertTrue(predicate.test(new CompanyBuilder().withStatus("applied").withTags("java").build()));
        assertTrue(predicate.test(new CompanyBuilder().withStatus("offered").withTags().build()));
    }

    @Test
    public void toStringMethod() {
        Optional<Status> status = Optional.of(new Status("applied"));
        List<String> tagKeywords = Arrays.asList("java", "remote");
        FilterPredicate predicate = new FilterPredicate(status, tagKeywords);

        String expected = FilterPredicate.class.getCanonicalName()
                + "{status=" + status + ", tagKeywords=" + tagKeywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
