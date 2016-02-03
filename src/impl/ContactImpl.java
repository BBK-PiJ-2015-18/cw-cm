package impl;

import interfaces.Contact;

public class ContactImpl implements Contact {

    private int id;

    private String name;

    private String notes;

    public ContactImpl(int id, String name, String notes) {
        validateId(id);
        validateNotNull(id, name, notes);

        this.id = id;
        this.name = name;
        this.notes = notes;
    }

    public ContactImpl(int id, String name) {
        validateId(id);
        validateNotNull(id, name);

        this.id = id;
        this.name = name;
        this.notes = null;
    }

    private void validateId(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id is 0 or negative");
        }
    }

    private void validateNotNull(Object... params) {
        for (Object item : params) {
            if(item == null) {
                throw new NullPointerException();
            }
        }
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