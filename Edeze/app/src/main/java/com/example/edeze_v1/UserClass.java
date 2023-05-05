package com.example.edeze_v1;

public class UserClass {
    public String authid;
    public String id;
    public double latitude;
    public double longitude;
    public String token;
    public boolean tutor;

    public UserClass(String authid, String id, double latitude, double longitude, String token, boolean tutor){
        this.authid = authid;
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        this.tutor = tutor;
    }
    public UserClass(){}
}
