package com.example.user.instaapp;

import java.util.ArrayList;
import java.util.List;

public class Followed_By {

    public String name;
    public int score;
    public List<InstaPhoto> photos;

    public Followed_By(){

    }

    public Followed_By(String name){
        this.name = name;
        photos = new ArrayList<>();
    }

    public void setScore(int score){
        this.score = score;
    }

    public void addPhoto(InstaPhoto photo){
        photos.add(photo);
    }

    public List<InstaPhoto> getPhotos(){
        return photos;
    }

    public String getName(){
        return name;
    }

    public int getScore() {
        return score;
    }
}
