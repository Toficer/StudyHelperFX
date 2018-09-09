package com.toficer.data;

public class Deck {
    private int _id;
    private String name;
    private String description;
    private int folderId;

    public Deck(int _id, String name, String description, int folderId) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.folderId = folderId;
    }

    public Deck() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    @Override
    public String toString(){
        return " " + name.toUpperCase();
    }
}
