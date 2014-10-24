package com.tripler.pdfreader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Ranjith on 10/24/2014.
 */
public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Bitmap> bmplist;

    public CustomAdapter(Context context, ArrayList<Bitmap> bmplist) {
        this.context = context;
        this.bmplist = bmplist;
    }

    @Override
    public int getCount() {
        return bmplist.size();
    }

    @Override
    public Object getItem(int position) {
        return bmplist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = null;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
            Bitmap bmp = bmplist.get(position);
            iv.setImageBitmap(bmp);
        }
        return convertView;
    }
}
