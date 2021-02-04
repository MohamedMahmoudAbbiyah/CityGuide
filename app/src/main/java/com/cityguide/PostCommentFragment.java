package com.cityguide;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.cityguide.PostClass.Post;
import com.cityguide.PostClass.PostAdapter;
import com.cityguide.dummy.DummyContent;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import CommentClass.Comment;
import CommentClass.CommentAdapter;

public class PostCommentFragment extends Fragment {


    static CommentAdapter adapter;
    ListView listViewcomment;
    Comment comment;
    static List<Comment> comments = new ArrayList<>();
    String locationName;

    public static PostCommentFragment newInstance(String locationName) {
        PostCommentFragment postCommentFragment = new PostCommentFragment();
        Bundle args = new Bundle();
        args.putString("locationName", locationName);
        postCommentFragment.setArguments(args);
        return postCommentFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            locationName = getArguments().getString("locationName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_comment_list, container, false);

        final DownloadComments downloadPosts = new DownloadComments();
        downloadPosts.execute("start");
        listViewcomment = view.findViewById(R.id.listviewcomment);

        //Set Adapter
        adapter = new CommentAdapter(comments, getActivity());
        listViewcomment.setAdapter(adapter);

        return view;
    }


    class DownloadComments extends AsyncTask<String, Integer, ArrayList<Comment>> {

        @Override
        protected ArrayList<Comment> doInBackground(String... strings) {
            comments.clear();
            //Get infos from Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comments");
            query.whereEqualTo("LocationName",locationName);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, com.parse.ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                comment = new Comment(object.getString("Username"), object.getString("LocationName"), object.getString("LocationComment"), object.getDouble("Rate"));
                                comments.add(comment);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                }
            });

            return null;
        }
    }
}