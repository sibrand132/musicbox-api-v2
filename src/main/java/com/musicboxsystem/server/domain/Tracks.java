package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by Sibrand on 2017-01-29.
 */
@Document
public class Tracks {


    @Id
    public String id;
    @NotNull(message = "Can't be empty")
    public String comment;

    public String membersId;

    public String bandsId;

    public String instrument;

    public String date;

    public Tracks(){}

    public Tracks(String comment, String membersId, String bandsId, String instrument, String date) {
        this.comment = comment;
        this.membersId = membersId;
        this.bandsId = bandsId;
        this.instrument = instrument;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMembersId() {
        return membersId;
    }

    public void setMembersId(String membersId) {
        this.membersId = membersId;
    }

    public String getBandsId() {
        return bandsId;
    }

    public void setBandsId(String bandsId) {
        this.bandsId = bandsId;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
