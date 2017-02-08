package com.musicboxsystem.server.domain;

/**
 * Created by Sibrand on 2017-02-08.
 */
public class Email {
    private String email;

    public Email(){}
    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
