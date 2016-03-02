package impl;

import interfaces.Contact;
import interfaces.Meeting;

import java.util.Calendar;
import java.util.Set;

public abstract class MeetingImpl implements Meeting {
    private int id;

    private Calendar date;

    private Set<Contact> contacts;

    public MeetingImpl(int id, Calendar date, Set<Contact> contacts) {
        ValidationHelpers.validateId(id);
        ValidationHelpers.validateNotNull(id, date, contacts);
        ValidationHelpers.validateHasItem(contacts);

        this.id = id;
        this.date = date;
        this.contacts = contacts;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Set<Contact> getContacts() {
        return this.contacts;
    }

    @Override
    public Calendar getDate() {
        return date;
    }
}
