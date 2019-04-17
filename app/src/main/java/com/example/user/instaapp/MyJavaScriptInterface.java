package com.example.user.instaapp;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MyJavaScriptInterface {

    String userId;
    String username;
    private DatabaseService databaseService = null;
    private AppPreferences appPreferences;
    private static JSONObject jsonObject;
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void processHTML(String html, boolean justUsername) {
        if (justUsername) {

            databaseService = DatabaseService.getInstance();
            // process the9 html as needed by the app

            String[] usernames = html.split("username");
            username = usernames[1].substring(usernames[1].indexOf(":"), usernames[1].indexOf(","));
            username.replace(":", "");
            username.replace(".", "");
            //   String[] names = username.split(".");
            //databaseService.getDatabaseReference().child("ana");
            User user = new User("user1");

            databaseService.addUser(user);
            Info.setUser(user);
        } else {
            String r = html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1);
           jsonObject = null;
            try {
                jsonObject = new JSONObject(r);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //    setUsername(jsonObject);
            //  username=":"bianca.maria.3994885"";

          /*  List<String> photos_urls = getPhotosUrl(jsonObject);
            List<String> mutual_followed_by = getMutualFollowedBy(jsonObject);
            System.out.println("HTML CONTENT" + html);*/

        }
    }

    public static JSONObject getJSONJsonObject(){
        return jsonObject;
    }

    public List<String> getMutualFollowedBy(JSONObject jsonObject) {
        List<String> followed_by_users = new ArrayList<>();
        try {
            String path = "/graphql/user/edge_mutual_followed_by";
            JSONObject result = getObject(jsonObject, extractKeys(path));
            JSONArray followed_by = result.getJSONArray("edges");

            for (int i = 0 ; i < followed_by.length(); i++) {
                JSONObject obj = followed_by.getJSONObject(i);
                path = "/node";
                JSONObject photo= getObject(obj, extractKeys(path));
                String username = photo.getString("username");
                followed_by_users.add(username);
            }
            System.out.println("Tralala");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return followed_by_users;
    }

    public Map<String, Integer> getPhotosUrl(JSONObject  jsonObject) {
        Map<String, Integer> photos_link = new HashMap<>();
        try {
            String path = "/graphql/user/edge_owner_to_timeline_media";
            JSONObject result = getObject(jsonObject, extractKeys(path));
            JSONArray photos = result.getJSONArray("edges");

            for (int i = 0 ; i < photos.length(); i++) {
                JSONObject obj = photos.getJSONObject(i);
                path = "/node";
                JSONObject photo= getObject(obj, extractKeys(path));
                String link = photo.getString("display_url");
                photos_link.put(link, 0);
            }
            System.out.println("Tralala");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos_link;
    }

    private String[] extractKeys(String path) {
        String leadingSlash = "/";
        if (!path.startsWith(leadingSlash))
            throw new RuntimeException("Path must begin with a leading '/'");

        return path.substring(1).split(leadingSlash);
    }

    public JSONObject getObject(JSONObject objectAPI, String[] keys) {
        String currentKey = keys[0];
        JSONObject nestedJsonObjectVal = objectAPI;

        if (keys.length == 1 && objectAPI.has(currentKey)) {
            try {
                return objectAPI.getJSONObject(currentKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (!objectAPI.has(currentKey)) {
            throw new RuntimeException(currentKey + "is not a valid key.");
        }

        try {
             nestedJsonObjectVal = objectAPI.getJSONObject(currentKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int nextKeyIdx = 1;
        String[] remainingKeys;
        remainingKeys = Arrays.copyOfRange(keys, nextKeyIdx, keys.length);
        return getObject(nestedJsonObjectVal, remainingKeys);
    }

    public void setUsername(JSONObject  jsonObject){

    }

    public String getUsername(){
        return "ana";
    }

}
