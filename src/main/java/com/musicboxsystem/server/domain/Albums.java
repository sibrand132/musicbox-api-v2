package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Sibrand on 2017-01-05.
 */
@Document
public class Albums {

    @Id
    public String id;
    public String title;
    public String about;
    public String releaseDate;
    public String BandsId;

    public Albums(){}
    public Albums(String title, String about, String releaseDate, String bandsId) {
        this.title = title;
        this.about = about;
        this.releaseDate = releaseDate;
        BandsId = bandsId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBandsId() {
        return BandsId;
    }

    public void setBandsId(String bandsId) {
        BandsId = bandsId;
    }
}
