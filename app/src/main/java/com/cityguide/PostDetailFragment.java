package com.cityguide;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cityguide.PostClass.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {

    private Post currentPost;

    TextView locationname,description,rating,category,distance;
    ImageView image;
    Button button_comments;
    ImageButton add_comments, add_favorites, navigation;
    String fav_locationname;
    ArrayList<String> favs = new ArrayList<>();



    public static PostDetailFragment newInstance(Post post) {
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("post",post);
        postDetailFragment.setArguments(args);
        return postDetailFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentPost = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        locationname = view.findViewById(R.id.locationname);
        description = view.findViewById(R.id.locationdescription);
        rating = view.findViewById(R.id.locationrating);
        distance = view.findViewById(R.id.locationdistance);
        category = view.findViewById(R.id.locationcategory);
        image = view.findViewById(R.id.locationimage);
        button_comments =  view.findViewById(R.id.btn_comments);
        add_comments =  view.findViewById(R.id.btn_addcomment);
        add_favorites =  view.findViewById(R.id.btn_addfavourites);
        navigation =  view.findViewById(R.id.navigate);

        DownloadFavs downloadFavs = new DownloadFavs();
        downloadFavs.execute("start");


        description.setText(currentPost.getLocationDescription());
        locationname.setText(currentPost.getLocationLocation());
        rating.setText(""+currentPost.getAverageRating());
        distance.setText(""+currentPost.getDistance());
        category.setText(currentPost.getCategoryName());
        image.setImageBitmap(currentPost.getImage());

        button_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostCommentFragment postCommentFragment = PostCommentFragment.newInstance(currentPost.getLocationLocation());
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postCommentFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        add_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCommentFragment addCommentFragment = AddCommentFragment.newInstance(currentPost.getLocationLocation(),currentPost);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,addCommentFragment);
                fts.addToBackStack(null);
                fts.commit();
            }
        });

        add_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_locationname = currentPost.getLocationLocation();
                if (favs.contains(fav_locationname)){
                    Toast.makeText(getActivity(), "Already Favorite!", Toast.LENGTH_LONG).show();
                }else {
                    ParseObject parseObject = new ParseObject("Favorites");
                    parseObject.put("FavoriteLocation", fav_locationname);
                    parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null){
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(), "Favorites Added!", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
            }
        });

        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MapsActivity.class);
                intent.putExtra("title",currentPost.getLocationLocation());
                intent.putExtra("latitude",currentPost.getLatitude());
                intent.putExtra("longitude",currentPost.getLongitude());
                startActivityForResult(intent,7);
            }
        });

        return view;
    }



    class DownloadFavs extends AsyncTask<String, Integer, ArrayList<Bitmap> > {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            favs.clear();
            //Get infos from Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Favorites");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                favs.add(object.getString("FavoriteLocation"));
                            }
                        }
                    }
                }
            });

            return null;
        }
    }
}