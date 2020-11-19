package com.example.youtubesubscriptionmanager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class channelActivity extends AppCompatActivity {
    final static String API = "&key=" + "AIzaSyC79zOEPQ3rw2fVyBLwuVvhU8YI7dyly80";
    final static String SEARCH_PREFIX_URL = "https://www.googleapis.com/youtube/v3/search";
    final static String PARTS = "?part=snippet";
    final static String TYPE = "&type=video";
    final static String ORDER = "&order=date";
    final static String MAX_RESULT = "&maxresult=5";

    String url = SEARCH_PREFIX_URL + PARTS + API + TYPE + ORDER + MAX_RESULT;
    String imgURL;

    ArrayList<Video> videoArrayList;
    ListView listView;
    RequestQueue requestQueue;

    //String channelID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_info);

        TextView channelName = findViewById(R.id.channel_name);

        Button ytButton = findViewById(R.id.youtube_button);
        Button subButton = findViewById(R.id.subscribe_button);
        Button homeButton = findViewById(R.id.home_button);
        Button searchButton = findViewById(R.id.search_button);

        ImageView imgView = findViewById(R.id.profile_pic);

        listView = findViewById(R.id.listView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) { // check if data is passed
            videoArrayList = new ArrayList<Video>();
            requestQueue = Volley.newRequestQueue(this);

            // get bundle data
            String channelID = bundle.getString("id");
            String imgURL = bundle.getString("img");
            String name = bundle.getString("name");
            String subtext = bundle.getString("subtext");

            // set up with bundle data
            channelName.setText(name);
            Picasso.with(getApplicationContext()).load(imgURL).into(imgView);

            // create api link
            String videoListURL = url + "&channelId=" + channelID;
            Log.e("id", channelID);

            // call api and create listView
            parseJSON(videoListURL);

            // when a video is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoArrayList.get(position).getId()));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/watch?v=" + videoArrayList.get(position).getId()));
                    try {
                        startActivity(appIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });

            //direct YOUTUBE button to open youtube in app or browser
            ytButton.setOnClickListener( v -> {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://channel/" + channelID));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/channel/" + channelID));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            });

            //check whether channel is subscribed
            SharedPreferences sharedPreferences = getSharedPreferences("subList", MODE_PRIVATE);
            int chCount = sharedPreferences.getInt("chCount", 0);
            for (int i = 0; i < chCount; i++) {
                String subbedID = sharedPreferences.getString( "sub" + i + "id", "");
                if ( subbedID.equals(channelID)) {
                    subButton.setText("Subscribed");
                }
            }

            //logic for subscribe button
            subButton.setOnClickListener( v -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (subButton.getText().toString() != "Subscribed") { //subscribe to channel
                    String subChannel = "sub" + Integer.toString(chCount);
                    editor.putString((subChannel + "name"), channelName.getText().toString());
                    editor.putString((subChannel + "img"), imgURL);
                    editor.putString((subChannel + "subtext"), subtext);
                    editor.putString((subChannel + "id"), channelID);
                    editor.putInt("chCount", chCount+1);
                    editor.commit();
                    subButton.setText("Subscribed");
                } else { //unsubscribe from channel
                    // find channel and shift every channel after it up the list
                    boolean found = false;
                    for (int i = 0; i < chCount; i++) {
                        if ( found ) {
                            String subChannel = "sub" + (i-1);
                            String replacement = "sub" + i;
                            editor.putString( subChannel + "name", sharedPreferences.getString(replacement + "name", "") );
                            editor.putString( subChannel + "img", sharedPreferences.getString(replacement + "img", "") );
                            editor.putString( subChannel + "subtext", sharedPreferences.getString(replacement + "subtext", "") );
                            editor.putString( subChannel + "id", sharedPreferences.getString(replacement + "id", "") );
                        } else {
                            String subbedID = sharedPreferences.getString( "sub" + i + "id", "");
                            if (subbedID.equals(channelID)) {
                                found = true;
                            }
                        }
                    }
                    subButton.setText("Subscribe");
                    editor.putInt("chCount", chCount-1);
                    editor.commit();
                }
            });
        }

        //menu button
        homeButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        searchButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, searchActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private synchronized void parseJSON(String URL) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray resultList = response.getJSONArray("items");
                    int length = resultList.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject row = resultList.getJSONObject(i).getJSONObject("snippet");

                        // read JSON
                        String title = row.getString("title");
                        String videoID = resultList.getJSONObject(i).getJSONObject("id").getString("videoId");
                        String date = row.getString("publishedAt").substring(0, Math.min(row.getString("publishedAt").length(), 10));
                        String imgURL = row.getJSONObject("thumbnails").getJSONObject("default").getString("url");
                        String upcoming = row.getString("liveBroadcastContent");

                        Video video = new Video(title, videoID, date, imgURL, upcoming);
                        videoArrayList.add(video);
                    }
                    VideoAdapter videoAdapter = new VideoAdapter(getApplicationContext(), videoArrayList);
                    listView.setAdapter(videoAdapter);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error getting JSON", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Youtube API not responding", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }
}
