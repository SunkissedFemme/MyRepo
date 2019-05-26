package com.example.user.instaapp;

import java.util.ArrayList;
import java.util.List;

public class Followed_By {

    public String name;
    public double score;
    public List<InstaPhoto> photos;
    public String categoryWithRisk;

    public Followed_By(){

    }

    public Followed_By(String name){
        this.name = name;
        photos = new ArrayList<>();
    }

    public void setScore(double score){
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

    public double getScore() {
        return score;
    }

    public void setCategory(String categoryWithRisk){
        this.categoryWithRisk=categoryWithRisk;
    }

    public String getCategory(){
        return categoryWithRisk;
    }
}
