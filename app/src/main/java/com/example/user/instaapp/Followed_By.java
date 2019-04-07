package com.example.user.instaapp;

import java.util.ArrayList;
import java.util.List;

public class Followed_By {

    public String name;
   // private int score;
    public List<String> photos;

    public Followed_By(){

    }

    public Followed_By(String name){
        this.name = name;
        photos = new ArrayList<>();
    }

    //public void setScore(int score){
     //   this.score = score;
   // }

    public void addPhoto(String photo_url){
        photos.add(photo_url);
    }

    //public List<String> getPhotos(){
      //  return photos;
    //}

    public String getName(){
        return name;
    }


}
