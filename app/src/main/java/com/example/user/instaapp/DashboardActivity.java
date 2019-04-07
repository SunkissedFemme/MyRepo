package com.example.user.instaapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;


import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;



import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageSource;


public class DashboardActivity extends AppCompatActivity {

    private AutoCompleteTextView nameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Button addChild_button = findViewById(R.id.add_child_button);
        Button save_button = findViewById(R.id.save_button);


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
        switch(view.getId()) {
            case R.id.save_button:{
                RequestInstagramAPI r = new RequestInstagramAPI();
                r.execute();
                System.out.println("BlaBla");
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
        }
    }

    private class RequestInstagramAPI extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Vision.Builder visionBuilder = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null);

            visionBuilder.setVisionRequestInitializer(
                    new VisionRequestInitializer("AIzaSyBiULo26hY9jDoN40KNc5uYO5_i3x_6tyk"));
            Vision vision = visionBuilder.build();

            ImageSource imgSource = new ImageSource();
            imgSource.set("imageUri", "https://scontent-otp1-1.cdninstagram.com/vp/da1993419f34f85b95bb6170735b6e46/5D3CE2C3/t51.2885-15/sh0.08/e35/c0.134.1080.1080/s640x640/53354676_858998621100108_1408139263129568011_n.jpg?_nc_ht=scontent-otp1-1.cdninstagram.com");

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
            try{
                BatchAnnotateImagesResponse batchResponse =
                        vision.images().annotate(batchRequest).execute();
                List<AnnotateImageResponse> responses = batchResponse.getResponses();
                System.out.println("Analiza");
            }catch(IOException ex){
                System.out.println(ex.getMessage());
            }

            return null;
        }
    }

}

