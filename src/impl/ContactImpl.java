package impl;

import interfaces.Contact;

public class ContactImpl implements Contact {

    private int id;

    private String name;

    private String notes;

    public ContactImpl(int id, String name, String notes) {
        ValidationHelpers.validateId(id);
        ValidationHelpers.validateNotNull(id, name, notes);

        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public ContactImpl(int id, String name) {
        ValidationHelpers.validateId(id);
        ValidationHelpers.validateNotNull(id, name);

        this.id = id;
        this.name = name;
        this.notes = null;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getNotes() {
        return notes;
    }

    @Override
    public void addNotes(String note) {
        this.notes += this.notes != null ? " " + note : note;
    }
}