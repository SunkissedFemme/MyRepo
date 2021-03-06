package com.example.user.instaapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageSource;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class DashboardActivity extends AppCompatActivity  implements  AuthenticationListener{

    private AutoCompleteTextView nameView;
    private EditText editText;
    //private ImageView imgV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button addChild_button = findViewById(R.id.add_child_button);
        Button save_button = findViewById(R.id.save_button);
       editText = findViewById(R.id.editTextAddFollowedBy);
      // imgV = findViewById(R.id.profImage);
       //Picasso.with(this).load("https://www.instagram.com/p/BxiOZtSJq4b/").into(imgV);


        //  nameView = findViewById(R.id.child);

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void onClick(View view) {
        String followed_by_name = editText.getText().toString();
        switch(view.getId()) {
            case R.id.save_button:{
                MyJavaScriptInterface javaScriptInterface = new MyJavaScriptInterface();
                JSONObject jsonObject = javaScriptInterface.getJSONJsonObject();
                final Followed_By followed_by_1= new Followed_By(followed_by_name);
                Map<String, Integer>  photosUrlsToScoreMap = javaScriptInterface.getPhotosUrl(jsonObject);
                Map<String, Integer> fewerPhotosUrlsToScoreMap = new HashMap<>();
                int i=0;
                for(String key : photosUrlsToScoreMap.keySet()){
                    if(i<=1) {
                        fewerPhotosUrlsToScoreMap.put(key, 0);
                        i++;
                    }
                    else{
                        break;
                    }
                }
               RequestInstagramAPI r = new RequestInstagramAPI(followed_by_1, photosUrlsToScoreMap);
                r.execute();

              /*  Child child = new Child("ana");
                Followed_By followed_by_1 = new Followed_By("fantastic");
               // followed_by_1.setScore(9);
                followed_by_1.addPhoto("photo1://http");
                child.addFollowedBy(followed_by_1);
                User u = Info.getUser();
                u.addChild(child);
                DatabaseService service = DatabaseService.getInstance();
                service.addChild(u, child);*/
             //   String child_name = nameView.getText().toString();

            }
            break;

            case R.id.add_child_button:{
                 MyJavaScriptInterface javaScriptInterface = new MyJavaScriptInterface();
                computeInfo(javaScriptInterface, followed_by_name);
/*
                JSONObject jsonObject = javaScriptInterface.getJSONJsonObject();
                Map<String, Integer>  photosUrlsToScoreMap = javaScriptInterface.getPhotosUrl(jsonObject);*/
                /*Followed_By followed_by_1 = new Followed_By("wolfiecindy");
                RequestInstagramAPI r = new RequestInstagramAPI(followed_by_1,photosUrlsToScoreMap);
                r.execute();*/

              /*  Child child = new Child("ana");
              //  Followed_By followed_by_1 = new Followed_By("wolfiecindy");
               // followed_by_1.setScore(9);
                child.addFollowedBy(followed_by_1);
                User u = Info.getUser();
                u.addChild(child);
                DatabaseService service = DatabaseService.getInstance();
                service.addChild(u, child);*/
                //   String child_name = nameView.getText().toString();

            }
            break;
        }
    }

    private void computeInfo(MyJavaScriptInterface javaScriptInterface, String followed_by_name) {
        WebView webView1 = AuthenticationDialog.getWebView();

        webView1.addJavascriptInterface(javaScriptInterface, "HTMLOUT");
        webView1.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String address)
            {
                // have the page spill its guts, with a secret prefix
                view.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>', false);");
            }
        });
        webView1.loadUrl(getResources().getString(R.string.get_user_info_url_first)+ followed_by_name + getResources().getString(R.string.get_user_info_url_second));
        webView1.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTokenReceived(String auth_token) {

    }

    @Override
    public void onInfoProcessed() {

    }

    private class RequestInstagramAPI extends AsyncTask<Void, Void, String> {

        private Map<String, Integer>  photosUrlsToScoreMap ;
        private Map<String, Integer> scoreMap;
        private Followed_By followed_by;

        private List<String> categoriesList;
        private Map<String, Double> scorePerCategoryMap;


        public RequestInstagramAPI(Followed_By followed_by, Map<String, Integer>  photosUrlsToScoreMap ){
            this.followed_by = followed_by;
            this.photosUrlsToScoreMap = photosUrlsToScoreMap;
            scoreMap = new HashMap<>();
            scoreMap.put("UNKNOWN", 0);
            scoreMap.put("VERY_UNLIKELY", 1);
            scoreMap.put("UNLIKELY", 2);
            scoreMap.put("POSSIBLE", 3);
            scoreMap.put("LIKELY", 4);
            scoreMap.put("VERY_LIKELY", 5);
            this.categoriesList = new ArrayList<>();
           // categoriesList.add("racy");
            categoriesList.add("medical");
            categoriesList.add("spoof");
            categoriesList.add("adult");
            categoriesList.add("violence");
            this.scorePerCategoryMap=new HashMap<>();
        }

        @Override
        protected String doInBackground(Void... params) {
            Vision.Builder visionBuilder = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null);

            visionBuilder.setVisionRequestInitializer(
                    new VisionRequestInitializer("AIzaSyBiULo26hY9jDoN40KNc5uYO5_i3x_6tyk"));
            Vision vision = visionBuilder.build();
            int score = 0;
            for (Map.Entry<String,Integer> entry : photosUrlsToScoreMap.entrySet()){
                //followed_by.addPhoto(entry.getKey());
                ImageSource imgSource = new ImageSource();
                imgSource.set("imageUri",entry.getKey());

                Image img = new Image();
                img.setSource(imgSource);
                Feature desiredFeature = new Feature();
                desiredFeature.setType("SAFE_SEARCH_DETECTION");
                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImage(img);
                request.setFeatures(Arrays.asList(desiredFeature));
                BatchAnnotateImagesRequest batchRequest =
                        new BatchAnnotateImagesRequest();

                batchRequest.setRequests(Arrays.asList(request));
                try {
                    BatchAnnotateImagesResponse batchResponse =
                            vision.images().annotate(batchRequest).execute();
                    List<AnnotateImageResponse> responses = batchResponse.getResponses();
                    Map<String, String>  safeAnnotationToScoreMap = new HashMap<>();
                    storeResponse(responses, safeAnnotationToScoreMap);
                    int scoreForFoto = getScore(responses);
                   // score = score + scoreForFoto;
                    photosUrlsToScoreMap.put(entry.getKey(),getScore(responses));
                    InstaPhoto photo = new InstaPhoto(entry.getKey(), safeAnnotationToScoreMap);
                    photo.setScore(scoreForFoto);
                    followed_by.addPhoto(photo);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }

            }
           // followed_by.setScore(score);
            for(String category: categoriesList){
                calculateTotalRiskPerCategory(category);
            }
            double maxScore = Collections.max(scorePerCategoryMap.values());
            String category = getMapKey(maxScore);
            if(maxScore==0){
                category ="Safe";
            }
            followed_by.setScore(maxScore);
            followed_by.setCategory(category);

          //  Child child = new Child("ana");
            User u = Info.getUser();
            Child child = u.getChildren().get(0);
            child.addFollowedBy(followed_by);

            DatabaseService service = DatabaseService.getInstance();
            service.addChild(u, child);
            return null;
        }

        private void calculateTotalRiskPerCategory(String category) {
            Map<String,  String> photoUrlsToMap=new HashMap<>();
            for(InstaPhoto photo: followed_by.getPhotos()){
               String value= photo.getSafeAnnotationToScoreMap().get(category);
               if(value.equals("POSSIBLE") || value.equals("LIKELY") || value.equals("VERY_LIKELY")){
                   photoUrlsToMap.put(photo.url, value);
               }
            }
            double sc = (double)photoUrlsToMap.entrySet().size()/ (double)followed_by.getPhotos().size();
            scorePerCategoryMap.put(category, sc);
        }

        private void storeResponse(List<AnnotateImageResponse> responses, Map<String, String>  safeAnnotationToScoreMap){
            for(AnnotateImageResponse imageResponse: responses){
              safeAnnotationToScoreMap.put("racy", (String)imageResponse.getSafeSearchAnnotation().get("racy"));
              safeAnnotationToScoreMap.put("medical", (String)imageResponse.getSafeSearchAnnotation().get("medical"));
              safeAnnotationToScoreMap.put("spoof", (String)imageResponse.getSafeSearchAnnotation().get("spoof"));
              safeAnnotationToScoreMap.put("adult", (String)imageResponse.getSafeSearchAnnotation().get("adult"));
              safeAnnotationToScoreMap.put("violence", (String)imageResponse.getSafeSearchAnnotation().get("violence"));
            }
        }

        private int getScore(List<AnnotateImageResponse> responses){
            int score = 0;
            for(AnnotateImageResponse imageResponse: responses){
                score = score +
                        scoreMap.get(imageResponse.getSafeSearchAnnotation().get("racy"))+
                        scoreMap.get(imageResponse.getSafeSearchAnnotation().get("medical"))+
                        scoreMap.get(imageResponse.getSafeSearchAnnotation().get("spoof"))+
                        scoreMap.get(imageResponse.getSafeSearchAnnotation().get("adult"))+
                        scoreMap.get(imageResponse.getSafeSearchAnnotation().get("violence"));
            }
            return score;
        }

        public String getMapKey(double value) {
            for (Map.Entry<String, Double> entry : scorePerCategoryMap.entrySet()) {
                if (entry.getValue().equals(value)) {
                    return entry.getKey();
                }
            }
            return null;
        }
    }

}

