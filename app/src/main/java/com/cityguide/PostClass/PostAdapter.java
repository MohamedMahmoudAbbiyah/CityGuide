package com.cityguide.PostClass;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cityguide.R;

import java.util.List;

public class PostAdapter extends ArrayAdapter<Post>{

    private List<Post> posts;
    private Activity context;

    public PostAdapter(List<Post> posts, Activity context) {
        super(context, R.layout.custom_view,posts);
        this.posts = posts;
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View customView = layoutInflater.inflate(R.layout.custom_view, null, true);
        TextView categorynameText = customView.findViewById(R.id.custom_comment_username);
        TextView locationnameText = customView.findViewById(R.id.custom_comment_locationname);
        ImageView imageView = customView.findViewById(R.id.custom_locationimage);
        TextView distanceText = customView.findViewById(R.id.custom_comment_comment);

        Post post = posts.get(position);

        categorynameText.setText(post.getCategoryName());
        locationnameText.setText(post.getLocationLocation());
        imageView.setImageBitmap(post.getImage());
        distanceText.setText(Double.toString(post.getDistance()));



        return customView;
    }
}
