package seedu.address.model.company;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class StatusTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Status((String) null));
        assertThrows(NullPointerException.class, () -> new Status((Status.Stage) null));
    }

    @Test
    public void constructor_invalidStatus_throwsUnsupportedStatusException() {
        String invalidStatus = "";
        assertThrows(
                seedu.address.model.company.exceptions.UnsupportedStatusException.class, () -> new Status(invalidStatus)
        );
    }

    @Test
    public void constructor_noArgument_createsDefaultStatus() {
        Status status = new Status();
        assertEquals(Status.Stage.TO_APPLY, status.value);
    }

    @Test
    public void isValidStatus() {
        // null status
        assertFalse(Status.isValidStatus(null));

        // invalid statuses
        assertFalse(Status.isValidStatus("")); // empty string
        assertFalse(Status.isValidStatus(" ")); // spaces only
        assertFalse(Status.isValidStatus("pending application")); // contains space
        assertFalse(Status.isValidStatus("technical*interview")); // contains special character
        assertFalse(Status.isValidStatus("hr_interview")); // contains underscore
        // valid statuses (only canonical five)
        assertTrue(Status.isValidStatus("to-apply"));
        assertTrue(Status.isValidStatus("applied"));
        assertTrue(Status.isValidStatus("in-process"));
        assertTrue(Status.isValidStatus("offered"));
        assertTrue(Status.isValidStatus("rejected"));
    }

    @Test
    public void equals() {
        Status status = new Status("to-apply");

        // same values -> returns true
        assertTrue(status.equals(new Status("to-apply")));

        // same object -> returns true
        assertTrue(status.equals(status));

        // null -> returns false
        assertFalse(status.equals(null));

        // different types -> returns false
        assertFalse(status.equals(5.0f));

        // different values -> returns false
        assertFalse(status.equals(new Status("in-process")));
    }

    @Test
    public void hashCode_sameStatus_returnsSameHashCode() {
        Status status1 = new Status("to-apply");
        Status status2 = new Status("to-apply");
        assertEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    public void hashCode_differentStatus_returnsDifferentHashCode() {
        Status status1 = new Status("to-apply");
        Status status2 = new Status("in-process");
        assertNotEquals(status1.hashCode(), status2.hashCode());
    }

    @Test
    public void toString_returnsCorrectValue() {
        Status status = new Status("in-process");
        assertEquals("in-process", status.toString());
    }

    @Test
    public void toString_defaultStatus_returnsDefaultValue() {
        Status status = new Status();
        assertEquals("to-apply", status.toString());
    }
}
