package com.example.user.instaapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        getUserInfoByAccessToken(token);
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
                    webView1.loadUrl(getResources().getString(R.string.get_user_info_url));
                    webView1.setVisibility(View.VISIBLE);
                    getUserInfoByAccessToken("token");
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
            try {
                URL urlObj = new URL(getResources().getString(R.string.get_user_info_url)+ token );
                HttpURLConnection urlConnection = (HttpURLConnection) urlObj.openConnection();
                InputStream is = urlConnection.getInputStream();
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
                    JSONObject jsonObject = new JSONObject(response);
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
