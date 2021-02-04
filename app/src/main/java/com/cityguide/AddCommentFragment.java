package com.cityguide;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cityguide.PostClass.Post;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class AddCommentFragment extends Fragment {

    String text_comment, locationName;
    double input_rate;
    EditText inputComment;
    RatingBar rate;
    Button post_comment;
    int commentCount;
    Post post;
    static ArrayList<Double> rates = new ArrayList<>();
    double final_rate = 0;



    public static AddCommentFragment newInstance(String locationName, Post post) {
        AddCommentFragment addCommentFragment = new AddCommentFragment();
        Bundle args = new Bundle();
        args.putString("locationName",locationName);
        args.putParcelable("post",post);
        addCommentFragment.setArguments(args);
        return addCommentFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationName = getArguments().getString("locationName");
            post = getArguments().getParcelable("post");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String currentUser = ParseUser.getCurrentUser().getUsername();
        View view = inflater.inflate(R.layout.fragment_add_comment, container, false);

        inputComment = view.findViewById(R.id.input_comment);
        rate = view.findViewById(R.id.rate_place);


        post_comment = view.findViewById(R.id.btn_postcomment);

        post_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_comment = inputComment.getText().toString();
                input_rate = rate.getRating();

                ParseObject parseObject = new ParseObject("Comments");
                parseObject.put("Username", currentUser);
                parseObject.put("LocationName", locationName);
                parseObject.put("LocationComment", text_comment);
                parseObject.put("Rate", input_rate);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(), "Comment Uploaded!", Toast.LENGTH_LONG).show();

                            DownloadRates downloadRates = new DownloadRates();
                            downloadRates.execute("start");
                        }
                    }
                });


            }
        });






        return view;
    }

    class DownloadRates extends AsyncTask<String, Integer, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            rates.clear();
            //Get infos from Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
            query.whereEqualTo("LocationName", post.getLocationLocation());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (objects.size() > 0) {
                            commentCount = objects.size();
                            double sum = 0;
                            for (final ParseObject object : objects) {
                                rates.add(object.getDouble("Rate"));
                                sum += object.getDouble("Rate");
                            }
                            final_rate = sum / rates.size();

                            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Posts");
                            query2.whereEqualTo("locationLocation", post.getLocationLocation());
                            query2.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    object.put("averageRating", final_rate);
                                    object.put("commentCount", commentCount);
                                    object.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            System.out.println("Updated!");
                                            post.setAverageRating(final_rate);
                                            post.setCommentCount(commentCount);

                                            PostDetailFragment postDetailFragment = PostDetailFragment.newInstance(post);
                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fts = fragmentManager.beginTransaction();
                                            fts.replace(R.id.container,postDetailFragment);
                                            fts.addToBackStack(null);
                                            fts.commit();
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            });

            return null;
        }
    }

}