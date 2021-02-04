package com.cityguide;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cityguide.PostClass.Post;
import com.cityguide.PostClass.PostAdapter;
import com.cityguide.dummy.DummyContent;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    static List<Post> fav_posts = new ArrayList<>();
    static List<Post> posts = new ArrayList<>();
    static PostAdapter adapter;
    ListView listView;
    Post post;
    LocationListener locationListener;
    LocationManager locationManager;
    LatLng userLocation;
    HaversineDistance haversineDistance = new HaversineDistance();
    double distance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites_list, container, false);

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


        final DownloadFavs downloadFavs = new DownloadFavs();
        downloadFavs.execute("start");


        //final DownloadMyPosts downloadMyPosts = new DownloadMyPosts();
        //downloadMyPosts.execute("start");



        listView = view.findViewById(R.id.fav_list);

        //Set Adapter
        adapter = new PostAdapter(fav_posts, getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Post post = fav_posts.get(position);
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

    class DownloadFavs extends AsyncTask<String, Integer, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
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
                                String fav_location = object.getString("FavoriteLocation");

                                fav_posts.clear();
                                //Get infos from Parse
                                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Posts");
                                query2.whereEqualTo("locationLocation", fav_location);
                                query2.findInBackground(new FindCallback<ParseObject>() {
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
                                                            if (e == null & data != null){
                                                                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                                                post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                                        object.getInt("commentCount"),  object.getDouble("averageRating"), object.getDouble("latitude"), object.getDouble("longitude"),
                                                                        bitmap);
                                                                distance = haversineDistance.haversine(userLocation.latitude,userLocation.longitude,post.getLatitude(),post.getLongitude());
                                                                distance = Double.parseDouble(new DecimalFormat("##.##").format(distance));
                                                                post.setDistance(distance);
                                                                fav_posts.add(post);
                                                                adapter.notifyDataSetChanged();
                                                                System.out.println(fav_posts.toString());
                                                            }
                                                        }
                                                    });
                                                }
                                            }
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

    class DownloadMyPosts extends AsyncTask<String, Integer, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {

            posts.clear();
            //Get infos from Parse
            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Posts");
            query2.findInBackground(new FindCallback<ParseObject>() {
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
                                        if (e == null & data != null & fav_posts.contains(object.getString("locationLocation"))){
                                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                                            post = new Post(object.getString("objectId"), object.getString("categoryName"), object.getString("locationDescription"), object.getString("locationLocation"),
                                                    object.getInt("commentCount"),  object.getInt("averageRating"), object.getInt("latitude"), object.getInt("longitude"),
                                                    bitmap);
                                            posts.add(post);
                                            adapter.notifyDataSetChanged();
                                            System.out.println(posts.toString());
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