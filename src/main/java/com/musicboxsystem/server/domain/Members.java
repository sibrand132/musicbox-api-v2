package com.musicboxsystem.server.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

/**
 * Created by Sibrand on 2017-01-04.
 */
@Document
public class Members {

    @Id
    public String id;
    @NotNull(message = "Can't be empty")
    public String bandsId;
    @NotNull(message = "Can't be empty")
    public String usersId;

    public Members(String bandsId, String usersId) {
        this.bandsId = bandsId;
        this.usersId = usersId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBandsId() {
        return bandsId;
    }

    public void setBandsId(String bandsId) {
        this.bandsId = bandsId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }
}
