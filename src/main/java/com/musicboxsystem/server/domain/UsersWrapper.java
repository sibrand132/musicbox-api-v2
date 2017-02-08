package com.musicboxsystem.server.domain;

import java.util.List;

/**
 * Created by Sibrand on 2017-02-08.
 */
public class UsersWrapper {
    private List<Users> usersList;

    public UsersWrapper(){}
    public UsersWrapper(List<Users> usersList) {
        this.usersList = usersList;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }


}
