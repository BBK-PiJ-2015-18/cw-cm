package impl;

import interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

public class ContactManagerImpl implements ContactManager {
    private final String fileName = "contacts.xml";

    private List<Meeting> meetings;

    private List<Contact> contacts;

    private int meetingIndex = 0;

    private int contactIndex = 0;

    public ContactManagerImpl() {
        meetings = new ArrayList<Meeting>();
        contacts = new ArrayList<Contact>();
    }

    private enum EntityType {
        Contact,
        Meeting
    }

    private int generateId(EntityType entityType) {
        switch (entityType) {
            case Contact:
                contactIndex++;
                return contactIndex;
            case Meeting:
                meetingIndex++;
                return meetingIndex;
            default:
                throw new IllegalArgumentException("Unknown entity type " + entityType.toString());
        }
    }

    @Override
    public int addFutureMeeting(Set<Contact> contacts, Calendar date) {
        ValidationHelpers.validateHasItem(contacts);
        ValidationHelpers.validateBeforeToday(date);
        ValidationHelpers.validateContainsAll(this.contacts, contacts);

        int id = generateId(EntityType.Meeting);
        Meeting newMeeting = new FutureMeetingImpl(id, date, contacts);
        this.meetings.add(newMeeting);

        return id;
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        return null;
    }

    @Override
    public Meeting getMeeting(int id) {
        return null;
    }

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        Calendar now = Calendar.getInstance();
        return meetings.stream().filter(meeting -> meeting.getDate().after(now) &&
                meeting.getContacts().contains(contact)).collect(Collectors.toList());
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        return null;
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        return null;
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        int newId = 1001;
        PastMeeting pastMeeting = new PastMeetingImpl(newId, date, contacts, text);
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        return null;
    }

    @Override
    public int addNewContact(String name, String notes) {
        int newId = 1001;
        Contact contact = new ContactImpl(newId, name, notes);
        return 0;
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        return null;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        return null;
    }

    @Override
    public void flush() {

    }
}
