package com.mynimef.swiracle.fragments.create;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.mynimef.swiracle.R;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Bitmap> {
    private int selectedId;
    private final boolean[] selectedField;
    private final PickImageFragment fragment;

    public ImageAdapter(Context context, ArrayList<Bitmap> images, PickImageFragment fragment) {
        super(context, R.layout.adapter_post, images);
        selectedField = new boolean[images.size()];
        selectedId = 0;
        selectedField[selectedId] = true;
        this.fragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Bitmap image = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_image,
                    null);
        }

        ImageView pic = (ImageView) convertView.findViewById(R.id.imageView);
        pic.setImageBitmap(image);

        if (selectedField[position]) {
            pic.setForeground(ContextCompat.getDrawable(getContext(),
                    R.drawable.foreground_image));
        } else {
            pic.setForeground(null);
        }

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedField[position]) {
                    selectedField[position] = true;
                    pic.setForeground(ContextCompat.getDrawable(getContext(),
                            R.drawable.foreground_image));
                    fragment.getImagePosition(position);

                    if (!fragment.getMultiple()) {
                        selectedField[selectedId] = false;
                        selectedId = position;
                        fragment.deleteImageBitmap(position);
                        notifyDataSetChanged();
                    }
                } else {
                    selectedField[position] = false;
                    pic.setForeground(null);
                }

            }
        });

        return convertView;
    }
}