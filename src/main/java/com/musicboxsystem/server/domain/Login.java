package com.musicboxsystem.server.domain;

/**
 * Created by Sibrand on 2017-01-31.
 */
public class Login {
    private String email;
    private String pass;

    public Login(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public Login(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
