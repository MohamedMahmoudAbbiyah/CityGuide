package com.cityguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fts.add(R.id.container,loginFragment);
        fts.commit();

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mainpage:
                CategoryFragment categoryFragment = new CategoryFragment();
                FragmentManager fragmentManager = this.getSupportFragmentManager();
                FragmentTransaction fts = fragmentManager.beginTransaction();
                fts.replace(R.id.container, categoryFragment);
                fts.addToBackStack(null);
                fts.commit();
                return true;

            case R.id.favourites:
                FavoritesFragment favoritesFragment = new FavoritesFragment();
                FragmentManager fragmentManager3 = this.getSupportFragmentManager();
                FragmentTransaction fts3 = fragmentManager3.beginTransaction();
                fts3.replace(R.id.container, favoritesFragment);
                fts3.addToBackStack(null);
                fts3.commit();
                return true;

        }
        return true;
    }

}