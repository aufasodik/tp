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
<<<<<<< HEAD
        assertFalse(Status.isValidStatus("tech interview")); // contains space
        assertFalse(Status.isValidStatus("online-assessment")); // legacy token
        // valid statuses
        assertTrue(Status.isValidStatus("to-apply"));
        assertTrue(Status.isValidStatus("applied"));
        assertTrue(Status.isValidStatus("oa"));
        assertTrue(Status.isValidStatus("tech-interview"));
        assertTrue(Status.isValidStatus("hr_interview"));
        assertTrue(Status.isValidStatus("in-process"));
        assertTrue(Status.isValidStatus("offered"));
        assertTrue(Status.isValidStatus("accepted"));
=======
        assertFalse(Status.isValidStatus("hr_interview")); // contains underscore
        // valid statuses (only canonical five)
        assertTrue(Status.isValidStatus("to-apply"));
        assertTrue(Status.isValidStatus("applied"));
        assertTrue(Status.isValidStatus("in-process"));
        assertTrue(Status.isValidStatus("offered"));
>>>>>>> 956392f7 (Enforce enums with 5 stages of application. Other statuses not accepted.)
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
<<<<<<< HEAD
        Status status = new Status("tech-interview");
        assertEquals("tech-interview", status.toString());
=======
        Status status = new Status("in-process");
        assertEquals("in-process", status.toString());
>>>>>>> 956392f7 (Enforce enums with 5 stages of application. Other statuses not accepted.)
    }

    @Test
    public void toString_defaultStatus_returnsDefaultValue() {
        Status status = new Status();
        assertEquals("to-apply", status.toString());
<<<<<<< HEAD
    }

    @Test
    public void fromStorage_legacyValues_throwsException() {
        assertThrows(
                seedu.address.model.company.exceptions.UnsupportedStatusException.class, () ->
                        Status.fromStorage("pending-application")
        );
        assertThrows(
                seedu.address.model.company.exceptions.UnsupportedStatusException.class, () ->
                        Status.fromStorage("online-assessment")
        );
        assertThrows(
                seedu.address.model.company.exceptions.UnsupportedStatusException.class, () ->
                        Status.fromStorage("offer-received")
        );
    }

    @Test
    public void fromStorage_canonicalValues_success() {
        assertEquals(Status.Stage.TO_APPLY, Status.fromStorage("to-apply"));
        assertEquals(Status.Stage.APPLIED, Status.fromStorage("applied"));
        assertEquals(Status.Stage.OA, Status.fromStorage("oa"));
        assertEquals(Status.Stage.TECH_INTERVIEW, Status.fromStorage("tech-interview"));
        assertEquals(Status.Stage.HR_INTERVIEW, Status.fromStorage("hr-interview"));
        assertEquals(Status.Stage.IN_PROCESS, Status.fromStorage("in-process"));
        assertEquals(Status.Stage.OFFERED, Status.fromStorage("offered"));
        assertEquals(Status.Stage.ACCEPTED, Status.fromStorage("accepted"));
        assertEquals(Status.Stage.REJECTED, Status.fromStorage("rejected"));
=======
>>>>>>> 956392f7 (Enforce enums with 5 stages of application. Other statuses not accepted.)
    }
}
