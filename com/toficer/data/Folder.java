package com.toficer.data;

public class Folder {
    private int _id;
    private String name;
    private String description;

    public Folder(int _id, String name, String description) {
        this._id = _id;
        this.name = name;
        this.description = description;
    }

    public Folder(){

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

    @Override
    public String toString(){
        return " " + name.toUpperCase();
    }
}
