package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by Sibrand on 2017-02-08.
 */
@Document
public class Songs {

    @Id
    public String id;
    public String date;
    @NotNull(message = "Can't be empty")
    public String title;
    public String bandsId;
    public String albumsId;
    public String uploaded;

    public Songs(){}
    public Songs(String date, String title, String bandsId, String albumsId, String uploaded) {
        this.date = date;
        this.title = title;
        this.bandsId = bandsId;
        this.albumsId = albumsId;
        this.uploaded = uploaded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBandsId() {
        return bandsId;
    }

    public void setBandsId(String bandsId) {
        this.bandsId = bandsId;
    }

    public String getAlbumsId() {
        return albumsId;
    }

    public void setAlbumsId(String albumsId) {
        this.albumsId = albumsId;
    }

    public String getUploaded() {
        return uploaded;
    }

    public void setUploaded(String uploaded) {
        this.uploaded = uploaded;
    }
}
