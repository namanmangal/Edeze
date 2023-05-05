package com.example.edeze_v1;

import java.util.ArrayList;
import java.util.List;
// db
public class tutorClass {
    private String identifier;
    private double location[]={0,0};
    private String city;
    private List<String> subj_can_teach = new ArrayList<String>();

    tutorClass(){
        identifier="";
        location[0]=0;
        location[1]=0;
        city="";
        subj_can_teach.removeAll(subj_can_teach);// empty list
    }

    tutorClass(String id, double latitude, double longitude, String city_basedIn, List subj_list){
        identifier = id;
        location[0] = latitude;
        location[1] = longitude;
        city = city_basedIn;
        subj_can_teach.addAll(subj_list);
        // copies everything from parameter list into this list.
    }

    String getIdentifier(){
        return identifier;
    }

    double getLatitude(){
        return location[0];
    }

    double getLongitude(){
        return location[1];
    }

    String getCity(){
        return city;
    }

    int getListLength(List<String> list){
        return list.size();
    }

    String getListContent(List<String> list,int listLocation){
        return list.get(listLocation);
    }
}
