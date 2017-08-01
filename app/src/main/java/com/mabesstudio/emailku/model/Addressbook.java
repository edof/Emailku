package com.mabesstudio.emailku.model;

/**
 * Created by Misbahul on 31/07/2017.
 */

public class Addressbook {
    private int id;
    private String name, email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
