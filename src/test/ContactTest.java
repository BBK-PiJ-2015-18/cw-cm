package test;

import impl.ContactImpl;
import interfaces.Contact;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContactTest {
    @Test
    public void canGetValues() {
        int id = 1001;
        String name = "bob";
        String notes = "annoying";
        Contact contact = new ContactImpl(id, name, notes);

        assertEquals(id, contact.getId());
        assertEquals(name, contact.getName());
        assertEquals(notes, contact.getNotes());
    }

    @Test
    public void canGetValuesRestrictedConstructor() {
        int id = 1001;
        String name = "bob";
        Contact contact = new ContactImpl(id, name);

        assertEquals(id, contact.getId());
        assertEquals(name, contact.getName());
        assertEquals(null, contact.getNotes());
    }

    @Test
    public void canAddMoreNotes() {
        int id = 1001;
        String name = "bob";
        String notes = "annoying";
        Contact contact = new ContactImpl(id, name, notes);

        String additionalNote1 = "slight sociopath";
        contact.addNotes(additionalNote1);

        String additionalNote2 = "loud";
        contact.addNotes(additionalNote2);

        assertEquals(notes + " " + additionalNote1 + " " + additionalNote2, contact.getNotes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenZeroId() {
        Contact contact = new ContactImpl(0, "bob", "123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenNegativeId() {
        Contact contact = new ContactImpl(-44, "bob", "123");
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenNullName() {
        Contact contact = new ContactImpl(1, null, "123");
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenNullNotes() {
        Contact contact = new ContactImpl(1, "bob", null);
    }
}