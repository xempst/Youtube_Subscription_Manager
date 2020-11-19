package com.example.youtubesubscriptionmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class searchActivity extends AppCompatActivity {
    final static String API_KEY = "&key=" + "AIzaSyC79zOEPQ3rw2fVyBLwuVvhU8YI7dyly80";
    final static String CHANNEL_SEARCH =  "&type=channel";
    final static String MAX_RESULT = "&maxResult=5";
    final static String PREFIX = "https://www.googleapis.com/youtube/v3/search?part=snippet";

    String URL = PREFIX + API_KEY + CHANNEL_SEARCH + MAX_RESULT;

    RequestQueue requestQueue;
    ArrayList<Channel> channelArrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requestQueue = Volley.newRequestQueue(this);
        channelArrayList = new ArrayList<Channel>();

        listView = findViewById(R.id.channel_listView);

        Button homeButton = findViewById(R.id.home_button);
        homeButton.setOnClickListener( (view) -> {
            Intent intent = new Intent( searchActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        TextInputEditText searchBar = findViewById(R.id.searchBar);
        Button searchButton = findViewById(R.id.applySearchButton);
        searchButton.setOnClickListener( (view) -> {
            channelArrayList.clear();       //clear last search
            String searchURL = URL + "&q=" + searchBar.getText().toString();
            parseJSON(searchURL);
        });

        // when a channel is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(searchActivity.this, channelActivity.class);
                intent.putExtra("id", channelArrayList.get(position).getId());
                intent.putExtra("name", channelArrayList.get(position).getName());
                intent.putExtra("img", channelArrayList.get(position).getImgURL());
                intent.putExtra("subtext", channelArrayList.get(position).getSubtext());
                startActivity(intent);
            }
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
                        channelArrayList.add(new Channel(row.getString("title"), row.getString("channelId"), row.getString("description"), row.getJSONObject("thumbnails").getJSONObject("high").getString("url") ) );
                    }
                    ChannelAdapter channelAdapter = new ChannelAdapter(getApplicationContext(), channelArrayList);
                    listView.setAdapter(channelAdapter);
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
