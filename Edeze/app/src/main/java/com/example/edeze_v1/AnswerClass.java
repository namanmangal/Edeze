package com.example.edeze_v1;
//db
public class AnswerClass {
    public String author;
    public String description;
    public int id;
    public int qid;

    public AnswerClass(String author, String description, int id, int qid){
        this.author = author;
        this.description = description;
        this.id = id;
        this.qid = qid;
    }
    public AnswerClass(){}
}
