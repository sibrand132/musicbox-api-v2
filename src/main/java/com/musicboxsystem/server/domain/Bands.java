package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Document
public class Bands {

    @Id
    public String id;

    @NotNull(message = "Can't be empty")
    @Size(max = 70, message = "Max 70")
    public String name;

    public String about;

    public String established;

    public String leader;

    public String status;

    public Bands(){}
    public Bands(String name, String about, String established, String leader, String status) {
        this.name = name;
        this.about = about;
        this.established = established;
        this.leader = leader;
        this.status = status;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getEstablished() {
        return established;
    }

    public String getLeader() {
        return leader;
    }

    public String getStatus() {
        return status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setEstablished(String established) {
        this.established = established;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
