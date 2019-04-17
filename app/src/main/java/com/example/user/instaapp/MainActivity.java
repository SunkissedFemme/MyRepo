package com.example.user.instaapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        implements NavigationView.OnNavigationItemSelectedListener {

    private String token = null;
    private AppPreferences appPreferences = null;
    private AuthenticationDialog authenticationDialog = null;
    private Button button = null;
    private View info = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /*@Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;
        appPreferences.putString(AppPreferences.TOKEN, auth_token);
        token = auth_token;
        // getUserInfoByAccessToken(token);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(this, DisplayPeopleActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



   /* @Override
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
*/
   /* private class RequestInstagramAPI extends AsyncTask<Void, Void, String> {

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


                       // login();
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
*/
}

