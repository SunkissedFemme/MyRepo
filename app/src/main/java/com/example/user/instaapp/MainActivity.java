package com.example.user.instaapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements AuthenticationListener {

    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button button = null;
    private View info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_login);
        info = findViewById(R.id.info);
        appPreferences = new AppPreferences(this);

        //check already have access token
        //    token = appPreferences.getString(AppPreferences.TOKEN);
        //   if (token != null) {
        //      getUserInfoByAccessToken(token);
        // }
    }


    public void login() {
        button.setText("LOGOUT");
        info.setVisibility(View.VISIBLE);
        TextView photo = findViewById(R.id.pic);
        photo.setText(appPreferences.getString(AppPreferences.PROFILE_PIC));
        //  ImageView pic = findViewById(R.id.pic);
        // Picasso.with(this).load(appPreferences.getString(AppPreferences.PROFILE_PIC)).into(pic);
        TextView id = findViewById(R.id.id);
        id.setText(appPreferences.getString(AppPreferences.USER_ID));
        TextView name = findViewById(R.id.name);
        name.setText(appPreferences.getString(AppPreferences.USER_NAME));
    }

    public void logout() {
        button.setText("INSTAGRAM LOGIN");
        token = null;
        info.setVisibility(View.GONE);
        appPreferences.clear();
    }

    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
       // getUserInfoByAccessToken(token);
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_login:
                if(token!=null)
                {
                    logout();
                }
                else {
                    authenticationDialog = new AuthenticationDialog(this, this);
                    authenticationDialog.setCancelable(true);
                    authenticationDialog.show();
                }
                break;

            case R.id.btn_info:
                if(authenticationDialog!=null){
                    System.out.println("Am fost pe aici");
                    WebView webView1 = authenticationDialog.getWebView();
                    //    authenticationDialog.initializeWebView(this.getResources().getString(R.string.get_user_info_url));
                    //   authenticationDialog.show();
                    webView1.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
                    webView1.setWebViewClient(new WebViewClient() {
                        public void onPageFinished(WebView view, String address)
                        {
                            // have the page spill its guts, with a secret prefix
                            view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        }
                    });
                    webView1.loadUrl(getResources().getString(R.string.get_user_info_url));
                    webView1.setVisibility(View.VISIBLE);

//                    getUserInfoByAccessToken("token");
                    authenticationDialog.show();

                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(),"haha!",Toast.LENGTH_LONG);
                    toast.show();
                }

                break;


        }

    }
    private void getUserInfoByAccessToken(String token) {
        RequestInstagramAPI r = new RequestInstagramAPI();
        r.execute();
    }

    private class RequestInstagramAPI extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder sb = new StringBuilder();
            InputStreamReader in;
            InputStream inputStream;
            try {
            //    URL urlObj = new URL(getResources().getString(R.string.get_user_info_url)+ token );
                URL urlObj = new URL(getResources().getString(R.string.get_user_info_url) );
                HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                int status = urlConnection.getResponseCode();

                if (status != HttpURLConnection.HTTP_OK)
                    inputStream = urlConnection.getErrorStream();
                else
                    inputStream= urlConnection.getInputStream();
                in = new InputStreamReader(urlConnection.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {
                        sb.append((char) cp);
                    }
                    bufferedReader.close();
                    in.close();
                }
                System.out.println("Async task for getting info");
                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override

        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    String r = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                    JSONObject jsonObject = new JSONObject(r);
                    Log.e("response", jsonObject.toString());
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if (jsonData.has("id")) {
                        System.out.println("Parse JSON");

                        appPreferences.putString(AppPreferences.USER_ID, jsonData.getString("id"));
                        appPreferences.putString(AppPreferences.USER_NAME, jsonData.getString("username"));
                        appPreferences.putString(AppPreferences.PROFILE_PIC, jsonData.getString("profile_picture"));


                        login();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"bla!",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

}

class MyJavaScriptInterface {
    @JavascriptInterface
    @SuppressWarnings("unused")
    public void processHTML(String html) {
        // process the html as needed by the app
        String r = html.substring(html.indexOf("{"), html.lastIndexOf("}") + 1);
        JSONObject  jsonObject = null;
        try {
            jsonObject = new JSONObject(r);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<String> photos_urls = getPhotosUrl(jsonObject);
        List<String> mutual_followed_by = getMutualFollowedBy(jsonObject);
        System.out.println("HTML CONTENT" + html);
    }

    private List<String> getMutualFollowedBy(JSONObject jsonObject) {
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

    private List<String> getPhotosUrl(JSONObject  jsonObject) {
        List<String> photos_link = new ArrayList<>();
        try {
            String path = "/graphql/user/edge_owner_to_timeline_media";
            JSONObject result = getObject(jsonObject, extractKeys(path));
            JSONArray photos = result.getJSONArray("edges");

            for (int i = 0 ; i < photos.length(); i++) {
                JSONObject obj = photos.getJSONObject(i);
                path = "/node";
                JSONObject photo= getObject(obj, extractKeys(path));
                String link = photo.getString("display_url");
                photos_link.add(link);
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

    private JSONObject getObject(JSONObject objectAPI, String[] keys) {
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

}
