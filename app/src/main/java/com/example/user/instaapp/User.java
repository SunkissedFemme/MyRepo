package com.example.user.instaapp;

import java.util.ArrayList;
import java.util.List;

public class User {

    public  String userID;
    public List<Child> children;

    public User(){

    }

    public User(String userID){
        this.userID = userID;
        children = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void addChild(Child child){
        children.add(child);
    }

    public List<Child> getChildren(){
        return children;
    }

}
