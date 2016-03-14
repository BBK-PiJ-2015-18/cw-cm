package test;

import impl.ContactImpl;
import impl.ContactManagerImpl;
import interfaces.Contact;
import interfaces.ContactManager;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class ContactManagerTest {
    public static final String EMPTY_STRING = "";

    private ContactManager contactManager;

    @Before
    public void setup() {
        contactManager = new ContactManagerImpl();
    }

    @After
    public void teardown() {
        contactManager.flush();
    }

    @Test(expected = NullPointerException.class)
    public void addNullContactName() {
        contactManager.addNewContact(null, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void addNullContactNotes() {
        contactManager.addNewContact(EMPTY_STRING, null);
    }

    @Test
    public void basicAddContact() {
        String name = "Bob";
        String notes = "Some notes.";
        int newId = contactManager.addNewContact(name, notes);

        Set<Contact> contactsByName = contactManager.getContacts(name);
        Contact contactByName = contactsByName.stream().findFirst().get();
        assertEquals(newId, contactByName.getId());
        assertEquals(name, contactByName.getName());
        assertEquals(notes, contactByName.getNotes());

        Set<Contact> contactsById = contactManager.getContacts(newId);
        Contact contactById = contactsById.stream().findFirst().get();
        assertEquals(newId, contactById.getId());
        assertEquals(name, contactById.getName());
        assertEquals(notes, contactById.getNotes());
    }

    @Test
    public void addAndSearchMultipleContacts() {
        String contact1Name = "John";
        String contact2Name = "Johanna";
        String contact3Name = "William";

        contactManager.addNewContact(contact1Name, EMPTY_STRING);
        contactManager.addNewContact(contact2Name, EMPTY_STRING);
        contactManager.addNewContact(contact3Name, EMPTY_STRING);

        Set<Contact> searchAll = contactManager.getContacts(EMPTY_STRING);
        assertEquals(3, searchAll.size());

        Set<Contact> searchJo = contactManager.getContacts("Jo");
        assertEquals(2, searchJo.size());

        Set<Contact> searchWill = contactManager.getContacts("Will");
        assertEquals(1, searchWill.size());

        assertEquals(contact3Name, searchWill.stream().findFirst().get().getName());
    }

    @Test
    public void addAndGetContactsById() {
        String contact1Name = "John";
        String contact2Name = "Johanna";
        String contact3Name = "William";

        int contact1Id = contactManager.addNewContact(contact1Name, EMPTY_STRING);
        int contact2Id = contactManager.addNewContact(contact2Name, EMPTY_STRING);
        int contact3Id = contactManager.addNewContact(contact3Name, EMPTY_STRING);

        Set<Contact> allContacts = contactManager.getContacts(contact1Id, contact2Id, contact3Id);
        assertEquals(3, allContacts.size());

        Set<Contact> firstContact = contactManager.getContacts(contact1Id);
        assertEquals(contact1Name, firstContact.stream().findFirst().get().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getContactsWithEmptyIds() {
        contactManager.getContacts(new int[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getContactsWithNonExistentIds() {
        int contactId = contactManager.addNewContact("Bob", EMPTY_STRING);
        contactManager.getContacts(contactId, 44, 2);
    }

    private Set<Contact> getBasicContactSet() {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(new ContactImpl(1, "Bob", EMPTY_STRING));
        return contacts;
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingWithPastDate() {
        Calendar date = GregorianCalendar.getInstance();
        date.add(Calendar.DAY_OF_YEAR, -1);
        contactManager.addFutureMeeting(getBasicContactSet(), date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingWithUnknownContact() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, 1);

        contactManager.addFutureMeeting(getBasicContactSet(), date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingWithUnknownContact2() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, 1);

        contactManager.addNewContact("Bob", EMPTY_STRING);
        contactManager.addFutureMeeting(getBasicContactSet(), date);
    }

    @Test(expected = NullPointerException.class)
    public void addFutureMeetingWithNullContacts() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, 1);

        contactManager.addFutureMeeting(null, date);
    }

    @Test(expected = NullPointerException.class)
    public void addFutureMeetingWithNullDate() {
        contactManager.addFutureMeeting(getBasicContactSet(), null);
    }

    @Test
    public void addFutureMeeting() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, 5);

        int contactId = contactManager.addNewContact("Bob", EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);

        int meetingId = contactManager.addFutureMeeting(contacts, date);

        Calendar secondMeetingDate = Calendar.getInstance();
        secondMeetingDate.add(Calendar.HOUR, 12);
        int secondMeetingId = contactManager.addFutureMeeting(contacts, date);

        FutureMeeting meeting = contactManager.getFutureMeeting(meetingId);
        assertEquals(meetingId, meeting.getId());
        assertEquals(date, meeting.getDate());
        assertEquals(contacts, meeting.getContacts());

        FutureMeeting secondMeeting = contactManager.getFutureMeeting(secondMeetingId);
        assertEquals(secondMeetingId, secondMeeting.getId());

        List<Meeting> meetings = contactManager.getFutureMeetingList(contacts.stream().findFirst().get());
        assertEquals(2, meetings.size());
    }

    @Test
    public void addFutureMeetingAndGetById() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, 5);

        int contactId = contactManager.addNewContact("Bob", EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);

        int meetingId = contactManager.addFutureMeeting(contacts, date);

        Meeting meeting = contactManager.getMeeting(meetingId);
        assertEquals(meetingId, meeting.getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFutureMeetingIsInPast() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, -5);

        int contactId = contactManager.addNewContact("Bob", EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);

        contactManager.addNewPastMeeting(contacts, date, EMPTY_STRING);

        contactManager.getFutureMeeting(1);
    }

    @Test
    public void getFutureMeetingNull() {
        Meeting meeting = contactManager.getFutureMeeting(1);
        assertEquals(null, meeting);
    }

    private Calendar createDateWithHours(int hours) {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.HOUR, hours);
        return date;
    }

    @Test
    public void getFutureMeetingListIsSorted() {
        int contactId = contactManager.addNewContact("Bob", EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);

        int meeting1Id = contactManager.addFutureMeeting(contacts, createDateWithHours(5));
        int meeting2Id = contactManager.addFutureMeeting(contacts, createDateWithHours(7));
        int meeting3Id = contactManager.addFutureMeeting(contacts, createDateWithHours(2));
        int meeting4Id = contactManager.addFutureMeeting(contacts, createDateWithHours(1));
        int meeting5Id = contactManager.addFutureMeeting(contacts, createDateWithHours(23));

        List<Meeting> meetings = contactManager.getFutureMeetingList(contacts.stream().findFirst().get());
        assertEquals(meeting4Id, meetings.get(0).getId());
        assertEquals(meeting3Id, meetings.get(1).getId());
        assertEquals(meeting1Id, meetings.get(2).getId());
        assertEquals(meeting2Id, meetings.get(3).getId());
        assertEquals(meeting5Id, meetings.get(4).getId());
    }

    @Test(expected = NullPointerException.class)
    public void getFutureMeetingListNullContact() {
        contactManager.getFutureMeetingList(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getFutureMeetingListInvalidContact() {
        contactManager.getFutureMeetingList(new ContactImpl(1, EMPTY_STRING, EMPTY_STRING));
    }

    @Test
    public void getMeetingListOn() {
        Calendar dateWithHours = createDateWithHours(5);

        String contactName = "Bob";
        int contactId = contactManager.addNewContact(contactName, EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);

        int meeting1Id = contactManager.addFutureMeeting(contacts, dateWithHours);
        int meeting2Id = contactManager.addFutureMeeting(contacts, dateWithHours);

        List<Meeting> meetings = contactManager.getMeetingListOn(dateWithHours);

        assertEquals(meeting1Id, meetings.get(0).getId());
        assertEquals(meeting2Id, meetings.get(1).getId());
    }

    @Test
    public void getMeetingNull() {
        Meeting meeting = contactManager.getMeeting(1);
        assertEquals(null, meeting);
    }

    @Test(expected = NullPointerException.class)
    public void addNewPastMeetingWithNullContacts() {
        contactManager.addNewPastMeeting(null, Calendar.getInstance(), EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void addNewPastMeetingWithNullCalendar() {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(new ContactImpl(1, "Bob", EMPTY_STRING));
        contactManager.addNewPastMeeting(contacts, null, EMPTY_STRING);
    }

    @Test(expected = NullPointerException.class)
    public void addNewPastMeetingWithNullNotes() {
        Set<Contact> contacts = new HashSet<>();
        contacts.add(new ContactImpl(1, "Bob", EMPTY_STRING));
        contactManager.addNewPastMeeting(contacts, Calendar.getInstance(), null);
    }

    @Test
    public void addNewPastMeeting() {
        Calendar dateWithHours = createDateWithHours(-5);

        String contactName = "Bob";
        int contactId = contactManager.addNewContact(contactName, EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);
        Contact contact = contacts.stream().findFirst().get();

        contactManager.addNewPastMeeting(contacts, dateWithHours, EMPTY_STRING);

        assertEquals(1, contactManager.getMeetingListOn(dateWithHours).size());
        assertEquals(1, contactManager.getPastMeetingListFor(contact).size());
        assertEquals(0, contactManager.getFutureMeetingList(contact).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addMeetingNotesMeetingDoesNotExist() {
        contactManager.addMeetingNotes(1, "Blah!");
    }

    @Test(expected = NullPointerException.class)
    public void addMeetingNotesMeetingNotesNull() {
        contactManager.addMeetingNotes(1, null);
    }

    @Test(expected = IllegalStateException.class)
    public void addMeetingNotesFutureMeeting() {
        String contactName = "Bob";
        int contactId = contactManager.addNewContact(contactName, EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);
        Contact contact = contacts.stream().findFirst().get();

        int meetingId = contactManager.addFutureMeeting(contacts, createDateWithHours(2));

        contactManager.addMeetingNotes(meetingId, "Blah!");
    }

    @Test
    public void addMeetingNotes() {
        String contactName = "Bob";
        int contactId = contactManager.addNewContact(contactName, EMPTY_STRING);
        Set<Contact> contacts = contactManager.getContacts(contactId);
        Contact contact = contacts.stream().findFirst().get();

        int meetingId = contactManager.addFutureMeeting(contacts, createDateWithHours(2));

        Meeting meeting = contactManager.getMeeting(meetingId);
        // Hack date
        meeting.getDate().add(Calendar.HOUR, -4);
        contactManager.addMeetingNotes(meetingId, "Blah!");
    }
}