package com.example.user.instaapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseService {

        private static DatabaseService single_instance = null;
        private String username;

        // variable of type String
        public DatabaseReference myRef;

        // private constructor restricted to this class itself
        private DatabaseService()
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
             myRef = database.getReference().child("users");
        }

        // static method to create instance of Singleton class
        public static DatabaseService getInstance()
        {
            if (single_instance == null)
                single_instance = new DatabaseService();

            return single_instance;
        }

        public DatabaseReference getDatabaseReference(){
            return myRef;
        }

        public void addChild(User user, Child c){
       //  myRef.child(user.getUserID()).child(c.getName()).setValue(c);
            myRef.child(user.getUserID()).setValue(user);
        }

        public void addUser(User user){
            myRef.child(user.getUserID()).setValue(user);
        }


        public void setUsername(String username){
            this.username = username;
        }

    }


