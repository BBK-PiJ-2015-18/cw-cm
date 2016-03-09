package test;

import impl.FutureMeetingImpl;
import interfaces.Contact;
import interfaces.FutureMeeting;

import java.util.Calendar;
import java.util.Set;

public class FutureMeetingTest extends MeetingTest<FutureMeeting> {
    @Override
    protected FutureMeeting getInstance(int id, Calendar date, Set<Contact> contacts) {
        return new FutureMeetingImpl(id, date, contacts);
    }
}
