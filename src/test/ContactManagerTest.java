package test;

import impl.ContactManagerImpl;
import interfaces.Contact;
import interfaces.ContactManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class ContactManagerTest {
    private ContactManager contactManager;

    @Before
    public void setup() {
        contactManager = new ContactManagerImpl();
    }

    @Test
    public void basicAddContact() {
        String name = "Bob";
        String notes = "";
        int newId = contactManager.addNewContact(name, notes);

        Set<Contact> contactsByName = contactManager.getContacts(name);
        Contact contactByName = contactsByName.stream().findFirst().get();
        Assert.assertEquals(newId, contactByName.getId());
        contactByName.getName().equals(name);

        Set<Contact> contactsById = contactManager.getContacts(newId);
        Contact contactById = contactsById.stream().findFirst().get();
        Assert.assertEquals(newId, contactById.getId());
        contactById.getName().equals(name);
    }
}