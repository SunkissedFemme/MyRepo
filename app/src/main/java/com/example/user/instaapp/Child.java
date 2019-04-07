package com.example.user.instaapp;

import java.util.ArrayList;
import java.util.List;

public class Child {

    public String name;
    public List<Followed_By> followed_by_list;

    public Child() {
    }

    public Child(String name) {
        this.name = name;
        followed_by_list = new ArrayList<>();
    }

    public void addFollowedBy(Followed_By entity){
        followed_by_list.add(entity);
    }

    public List<Followed_By>  getFollowedByList(){
        return followed_by_list;
    }

    public String getName() {
    return name;
    }

}
