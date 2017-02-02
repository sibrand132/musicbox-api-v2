package com.musicboxsystem.server.domain;

/**
 * Created by Sibrand on 2017-02-01.
 */
public class Token {

    private String token;

    public Token(){}
    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
