package test;

import impl.ContactImpl;
import impl.PastMeetingImpl;
import interfaces.Contact;
import interfaces.Meeting;
import interfaces.PastMeeting;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class PastMeetingTest extends MeetingTest<PastMeeting> {

    @Override
    protected PastMeeting getInstance(int id, Calendar date, Set<Contact> contacts) {
        return new PastMeetingImpl(id, date, contacts, "");
    }

    @Test
    public void canGetValues() {
        int id = 1001;
        Calendar date = Calendar.getInstance();
        Set<Contact> contacts = new HashSet<Contact>();
        contacts.add(new ContactImpl(1, "Bob"));
        contacts.add(new ContactImpl(2, "Bill"));
        contacts.add(new ContactImpl(3, "John"));

        String notes = "";
        PastMeeting meeting = new PastMeetingImpl(id, date, contacts, notes);

        assertEquals(id, meeting.getId());
        assertEquals(date, meeting.getDate());
        assertEquals(contacts, meeting.getContacts());
        assertEquals(notes, meeting.getNotes());
    }

    @Test(expected = NullPointerException.class)
    public void throwsWhenNullNotes() {
        int id = 1;
        Set<Contact> contacts = new HashSet<Contact>();
        contacts.add(new ContactImpl(1, "Bob"));
        Calendar date = new GregorianCalendar(2015, 01, 02);
        String notes = null;

        Meeting meeting = new PastMeetingImpl(id, date, contacts, notes);
    }
}