package impl;

import interfaces.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContactManagerImpl implements ContactManager, Serializable {
    private static final long serialVersionUID = 4L;

    private final String FILE_NAME = "contacts.xml";

    private final String EMPTY_STRING = "";

    private List<PastMeeting> pastMeetings;

    private List<FutureMeeting> futureMeetings;

    private List<Contact> contacts;

    private int meetingIndex = 0;

    private int contactIndex = 0;

    public ContactManagerImpl() {
        pastMeetings = new ArrayList<>();
        futureMeetings = new ArrayList<>();
        contacts = new ArrayList<>();

        loadFileData();
    }

    private void loadFileData() {
        File file = new File(FILE_NAME);
        if (!file.exists() || !file.isDirectory()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ObjectInputStream inputStream = null;
            try {
                inputStream = new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(FILE_NAME)));
            } catch (EOFException ex) {
                // File is empty
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                contacts = (List<Contact>) inputStream.readObject();
                pastMeetings = (List<PastMeeting>) inputStream.readObject();
                futureMeetings = (List<FutureMeeting>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        ValidationHelpers.validateNotNull(contacts, date);
        ValidationHelpers.validateHasItem(contacts);
        ValidationHelpers.validateNotBeforeNow(date);
        ValidationHelpers.validateContainsAll(this.contacts, contacts);

        int id = generateId(EntityType.Meeting);
        FutureMeeting newMeeting = new FutureMeetingImpl(id, date, contacts);
        this.futureMeetings.add(newMeeting);

        return id;
    }

    @Override
    public PastMeeting getPastMeeting(int id) {
        return null;
    }

    @Override
    public FutureMeeting getFutureMeeting(int id) {
        Meeting meeting = getMeeting(id);
        if(meeting == null) {
            return null;
        }

        if(pastMeetings.contains(meeting)) {
            throw new IllegalArgumentException("Meeting in past");
        }

        return (FutureMeeting) meeting;
    }

    @Override
    public Meeting getMeeting(int id) {
        Optional<Meeting> first = getAllMeetingsStream().filter(m -> m.getId() == id).findFirst();
        return first.isPresent() ? first.get() : null;
    }

    private Stream<Meeting> getAllMeetingsStream() {
        return Stream.concat(pastMeetings.stream(), futureMeetings.stream());
    }

    private Comparator<Meeting> meetingComparator = (o1, o2) -> o1.getDate().compareTo(o2.getDate());

    @Override
    public List<Meeting> getFutureMeetingList(Contact contact) {
        ValidationHelpers.validateNotNull(contact);
        ValidationHelpers.validateContains(contacts, contact);

        Calendar now = Calendar.getInstance();
        return futureMeetings.stream().filter(meeting -> meeting.getDate().after(now) &&
                meeting.getContacts().contains(contact)).sorted(meetingComparator).collect(Collectors.toList());
    }

    @Override
    public List<Meeting> getMeetingListOn(Calendar date) {
        ValidationHelpers.validateNotNull(date);
        return getAllMeetingsStream().filter(m -> m.getDate().equals(date)).sorted(meetingComparator).collect(Collectors.toList());
    }

    @Override
    public List<PastMeeting> getPastMeetingListFor(Contact contact) {
        ValidationHelpers.validateNotNull(contact);
        ValidationHelpers.validateContains(contacts, contact);

        Calendar now = Calendar.getInstance();
        return pastMeetings.stream().filter(meeting -> meeting.getDate().before(now) &&
                meeting.getContacts().contains(contact)).sorted(meetingComparator).collect(Collectors.toList());
    }

    @Override
    public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text) {
        ValidationHelpers.validateNotNull(contacts, date, text);
        ValidationHelpers.validateHasItem(contacts);
        ValidationHelpers.validateBeforeNow(date);
        ValidationHelpers.validateContainsAll(this.contacts, contacts);

        int id = generateId(EntityType.Meeting);
        PastMeeting pastMeeting = new PastMeetingImpl(id, date, contacts, text);
        this.pastMeetings.add(pastMeeting);
    }

    @Override
    public PastMeeting addMeetingNotes(int id, String text) {
        ValidationHelpers.validateNotNull(text);

        Meeting meeting = getMeeting(id);
        if (meeting == null) {
            throw new IllegalArgumentException();
        }

        Calendar now = Calendar.getInstance();

        if (meeting.getDate().after(now)) {
            throw new IllegalStateException();
        }

        futureMeetings.remove(meeting);
        PastMeeting pastMeeting = new PastMeetingImpl(meeting.getId(), meeting.getDate(), meeting.getContacts(), text);
        pastMeetings.add(pastMeeting);

        return pastMeeting;
    }

    @Override
    public int addNewContact(String name, String notes) {
        ValidationHelpers.validateNotNull(name, notes);

        int id = generateId(EntityType.Contact);
        Contact contact = new ContactImpl(id, name, notes);
        this.contacts.add(contact);
        return id;
    }

    @Override
    public Set<Contact> getContacts(int... ids) {
        ValidationHelpers.validateHasItem(ids);

        Set<Contact> results = new HashSet<>();

        // Check that all ids are valid
        for(int id : ids) {
            Optional<Contact> contact = contacts.stream().filter(c -> c.getId() == id).findFirst();
            if(contact.isPresent()) {
                results.add(contact.get());
            } else {
                throw new IllegalArgumentException("Invalid contact id.");
            }
        }

        return results;
    }

    @Override
    public Set<Contact> getContacts(String name) {
        if (Objects.equals(name, EMPTY_STRING)) {
            return contacts.stream().collect(Collectors.toSet());
        }

        return contacts.stream().filter(contact -> contact.getName().contains(name)).collect(Collectors.toSet());
    }

    @Override
    public void flush() {
        ObjectOutputStream encode = null;
        try {
            encode = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(encode == null) {
            System.err.println("Something went wrong - couldn't serialize");
            return;
        }

        try {
            encode.writeObject(pastMeetings);
            encode.writeObject(futureMeetings);
            encode.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            encode.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}