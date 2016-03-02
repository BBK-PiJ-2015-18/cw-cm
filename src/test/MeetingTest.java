package test;

import impl.ContactImpl;
import impl.PastMeetingImpl;
import interfaces.Contact;
import interfaces.Meeting;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public abstract class MeetingTest<T extends Meeting> {

    protected abstract T getInstance(int id, Calendar date, Set<Contact> contacts);

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenZeroId() {
        int id = 0;
        Meeting meeting = getInstance(id, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenNegativeId() {
        int id = -5;
        Meeting meeting = new PastMeetingImpl(id, null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenNullDate() {
        int id = 1;
        Set<Contact> contacts = new HashSet<Contact>();
        contacts.add(new ContactImpl(1, "Bob"));
        String notes = "Blah";

        Meeting meeting = new PastMeetingImpl(id, null, contacts, notes);
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenNullContacts() {
        int id = 1;
        Calendar date = new GregorianCalendar(2015, 01, 02);
        String notes = "Blah";

        Meeting meeting = new PastMeetingImpl(id, date, null, notes);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsWhenEmptyContacts() {
        int id = 1;
        Set<Contact> contacts = new HashSet<Contact>();
        Calendar date = new GregorianCalendar(2015, 01, 02);
        String notes = "Blah";

        Meeting meeting = new PastMeetingImpl(id, date, contacts, notes);
    }
}
