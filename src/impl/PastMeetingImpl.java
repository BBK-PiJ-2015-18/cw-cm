package impl;

import interfaces.Contact;
import interfaces.PastMeeting;

import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {

    private String notes;

    public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts, String notes) {
        super(id, date, contacts);
        ValidationHelpers.validateNotNull(notes);
        this.notes = notes;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void addNotes(String note) {
        notes += note;
    }
}
