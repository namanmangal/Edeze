package com.example.edeze_v1;

import android.os.Parcelable;

import java.io.Serializable;

public class QuestionClass {
    public int id;
    public String author;
    public String title;
    public String description;

    public QuestionClass(int id, String author, String title, String description){
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
    }
    public QuestionClass(){}
}
