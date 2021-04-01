package com.mynimef.swiracle.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.mynimef.swiracle.FragmentConnector;
import com.mynimef.swiracle.Post;
import com.mynimef.swiracle.ui.post.PostFragment;
import com.mynimef.swiracle.PostList;
import com.mynimef.swiracle.R;

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(Context context, PostList list) {
        super(context, R.layout.adapter_post, list.getList());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Post post = getItem(position);
        FragmentConnector fc = (FragmentConnector) getContext();

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_post, null);
        }

        Button title = (Button) convertView.findViewById(R.id.button);
        title.setText(post.getTitle());
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        ImageView pic = convertView.findViewById(R.id.imageView);
        pic.setImageResource(post.getImageResource());
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.replaceFragment(new PostFragment());
            }
        });

        Button description = (Button) convertView.findViewById(R.id.description);
        description.setText(post.getDescription());
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button likes = (Button) convertView.findViewById(R.id.likes);
        likes.setText(post.getLikes() + "");
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.increaseLikes();
                likes.setText(post.getLikes() + "");
            }
        });

        Button comments = (Button) convertView.findViewById(R.id.comments);
        comments.setText(post.getComments() + "");
        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.increaseComments();
                comments.setText(post.getComments() + "");
            }
        });

        return convertView;
    }
}