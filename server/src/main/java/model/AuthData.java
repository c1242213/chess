package model;

import java.util.UUID;

public class AuthData {
    String authToken;
    String Username;

    public AuthData(){
        this.authToken = UUID.randomUUID().toString();
    }

    public String getAuthToken() {
        return authToken;
    }

//    public void setAuthToken(String authToken) {
//        this.authToken = authToken;
//    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
