package com.example.user.instaapp;

public class Info {
    /*private static Info single_instance = null;
    public static Info getInstance()
    {
        if (single_instance == null)
            single_instance = new Info();

        return single_instance;
    }*/

    private static User user;

    public static void setUser(User myuser){
        user = myuser;
    }

    public static User getUser(){
        return user;
    }
}
