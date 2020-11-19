package com.example.youtubesubscriptionmanager;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoAdapter implements ListAdapter {
    ArrayList<Video> arrayList;
    Context context;

    VideoAdapter(Context context, ArrayList<Video> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Video video = arrayList.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_video, null);

            TextView videoTitle = convertView.findViewById(R.id.video_title);
            TextView videoDate = convertView.findViewById(R.id.video_date);
            TextView videoUpcoming = convertView.findViewById(R.id.upcoming_text);
            ImageView imgView = convertView.findViewById(R.id.video_thumbnail);

            videoTitle.setText(video.getTitle());
            videoDate.setText(video.getDate());
            if (video.getUpcoming().equals("upcoming")) {
                videoUpcoming.setText("upcoming");
            } else {
                videoUpcoming.setText("");
            }
            Picasso.with(context).load(video.getImgURL()).into(imgView);
        }

        return convertView;
    }

    // from https://www.tutorialspoint.com/how-to-display-a-list-of-images-and-text-in-a-listview-in-android
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}
