package com.example.user.instaapp;

import java.util.HashMap;
import java.util.Map;

public class InstaPhoto {

    public String url;
    public Map<String, String> safeAnnotationToScoreMap = new HashMap<>();
    public int score;

    public InstaPhoto(){

    }

    public InstaPhoto(String url, Map<String, String> safeAnnotationToScoreMap){
        this.url = url;
        this.safeAnnotationToScoreMap=safeAnnotationToScoreMap;
    }

    public void setScore(int score){
        this.score=score;
    }

}
