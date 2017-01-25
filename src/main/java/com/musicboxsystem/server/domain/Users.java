package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.jws.soap.SOAPBinding;
import javax.validation.constraints.NotNull;

/**
 * Created by Sibrand on 2017-01-04.
 */

@Document
public class Users {

    @Id
    public String id;

    @NotNull(message = "Can't be empty")
    public String name;

    @NotNull(message = "Can't be empty")
    public String email;

    @NotNull(message = "Can't be empty")
    public String role;

    @NotNull(message = "Can't be empty")
    public String pass;

    public Users(){}
    public Users(String name, String email, String role, String pass) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.pass = pass;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPass() {
        return pass;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

