package com.example.youtubesubscriptionmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    final static String API_KEY = "&key=" + "AIzaSyCxHH_pcIVs2vtQPrFKAkedBHZJPukDxxI";

    ArrayList<Channel> channelArrayList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_list);

        // check for internet connection
        int internetPermissionGrated = ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if (internetPermissionGrated != PackageManager.PERMISSION_GRANTED) {
            final String[] permission = new String[] {Manifest.permission.INTERNET};
            ActivityCompat.requestPermissions(this, permission, 2);
        }

        channelArrayList = new ArrayList<>();
        listView = findViewById(R.id.sub_listview);

        // menu button
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener( v -> {
            Intent intent = new Intent(this, searchActivity.class);
            startActivity(intent);
            finish();
        });

        // load subscription
        SharedPreferences sharedPreferences = getSharedPreferences("subList", MODE_PRIVATE);
        int chCount = sharedPreferences.getInt("chCount", 0);
        if (chCount > 0) {
            for (int i = 0; i < chCount; i++) {
                String subChannel = "sub" + i;
                String name = sharedPreferences.getString(subChannel + "name", "");
                String img = sharedPreferences.getString(subChannel + "img", "");
                String subtext = sharedPreferences.getString(subChannel + "subtext", "");
                String id = sharedPreferences.getString(subChannel + "id", "");
                Channel channel = new Channel(name, id, subtext, img);
                channelArrayList.add(channel);
            }
            ChannelAdapter channelAdapter = new ChannelAdapter(this, channelArrayList);
            listView.setAdapter(channelAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, channelActivity.class);
                intent.putExtra("id", channelArrayList.get(position).getId());
                intent.putExtra("name", channelArrayList.get(position).getName());
                intent.putExtra("img", channelArrayList.get(position).getImgURL());
                intent.putExtra("subtext", channelArrayList.get(position).getSubtext());
                startActivity(intent);
                finish();
            }
        });
    }
}