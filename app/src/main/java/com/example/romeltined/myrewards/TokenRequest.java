package com.example.romeltined.myrewards;

/**
 * Created by romeltined on 8/22/2017.
 */
public class TokenRequest {
    public String client_id;
    public String grant_type;
    public String username;
    public String password;

    public TokenRequest(String client_id, String grant_type, String username, String password) {
        this.client_id = client_id;
        this.grant_type = grant_type;
        this.username = username;
        this.password = password;
    }
}
