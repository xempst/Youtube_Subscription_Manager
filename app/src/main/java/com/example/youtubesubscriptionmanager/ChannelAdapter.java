package com.example.youtubesubscriptionmanager;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ChannelAdapter implements ListAdapter {
    ArrayList<Channel> arrayList;
    Context context;

    ChannelAdapter(Context context, ArrayList<Channel> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Channel channel = arrayList.get(position);

        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_channel, null);

            TextView channelName = convertView.findViewById(R.id.channel_name);
            TextView subtext = convertView.findViewById(R.id.subtext);
            ImageView imgView = convertView.findViewById(R.id.thumbnail);

            channelName.setText(channel.getName());
            subtext.setText(channel.getSubtext());
            Picasso.with(context).load(channel.getImgURL()).into(imgView);
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
