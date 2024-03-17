package model;

import java.util.UUID;

public class AuthData {
    String authToken;
    String username;

    public AuthData(){
        this.authToken = UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
    }
}
