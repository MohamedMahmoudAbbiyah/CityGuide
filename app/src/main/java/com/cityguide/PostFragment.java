package com.cityguide;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.cityguide.PostClass.Post;
import com.cityguide.PostClass.PostAdapter;
import com.cityguide.PostClass.User;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static androidx.core.content.ContextCompat.getSystemService;

public class PostFragment extends Fragment {

    static PostAdapter adapter;
    ListView listView;
    static List<Post> posts = new ArrayList<>();
    static List<Post> toprated_posts = new ArrayList<>();
    String category;
    Post post;
    LocationListener locationListener;
    LocationManager locationManager;
    LatLng userLocation;
    HaversineDistance haversineDistance = new HaversineDistance();
    double distance;


    public static PostFragment newInstance(String category) {
        PostFragment postFragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        postFragment.setArguments(args);
        return postFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            category = getArguments().getString("category");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        listView = view.findViewById(R.id.listview);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(),location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (lastLocation != null){
                userLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            }
        }

        final DownloadPosts downloadPosts = new DownloadPosts(category);
        downloadPosts.execute("start");

        //Set Adapter
        adapter = new PostAdapter(posts, getActivity());
        listView.setAdapter(adapter);


        //List Object Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post post = posts.get(position);
                PostDetailFragment postDetailFragment = PostDetailFragment.newInstance(post);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container,postDetailFragment);
                fts.addToBackStack(null);
                fts.commit();

            }
        });


        return view;
    }


    class DownloadPosts extends AsyncTask<String, Integer, ArrayList<Bitmap> > {
        String categorychose;

        public DownloadPosts(String categorychose) {
            this.categorychose = categorychose;
        }
//object.getString("categoryName").equals("Historic")
        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            posts.clear();
            //Get infos from Parse
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Posts");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (objects.size() > 0) {
                            for (final ParseObject object : objects) {
                                ParseFile parseFile = (ParseFile) object.get("image");
                                parseFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        if (e == null & data != null & categorychose.equals("all")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                            object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                                bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();

                                        }else if (e == null & data != null & categorychose.equals("historic") & object.getString("categoryName").equals("Historic")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();

                                        }else if (e == null & data != null & categorychose.equals("entertainment") & object.getString("categoryName").equals("Entertainment")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();

                                        }else if (e == null & data != null & categorychose.equals("museum") & object.getString("categoryName").equals("Museums")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();

                                        }
                                        else if (e == null & data != null & categorychose.equals("eating") & object.getString("categoryName").equals("Eating")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();

                                        }
                                        else if (e == null & data != null & categorychose.equals("shopping") & object.getString("categoryName").equals("Shopping")){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();
                                        }else if (e == null & data != null & (categorychose.equals("toprated") || categorychose.equals("mostcomments") || categorychose.equals("closest"))){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                    bitmap);
                                            distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                            distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                            post.setDistance(distance);
                                            if (categorychose.equals("toprated")){
                                                post.setCompareString("toprated");
                                            }else if (categorychose.equals("mostcomments")){
                                                post.setCompareString("mostcomments");
                                            }else if (categorychose.equals("closest")){
                                                for(Post post: posts){
                                                    if (post.getDistance() > 50){
                                                        posts.remove(post);
                                                    }
                                                }
                                                post.setCompareString("closest");
                                            }
                                            posts.add(post);
                                            Collections.sort(posts);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });

            return null;
        }
    }

}