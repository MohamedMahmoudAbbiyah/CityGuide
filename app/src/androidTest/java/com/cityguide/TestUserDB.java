package com.cityguide;

import android.content.Intent;
import android.widget.Toast;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cityguide.PostClass.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import CommentClass.Comment;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class TestUserDB {

    @Before
    public void insertUser() {
        ParseUser user = new ParseUser();
        user.setUsername("deneme");
        user.setPassword("deneme");
        user.setEmail("deneme@gmail.com");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                System.out.println("Done!");
            }
        });
    }

    @Test
    public void checkUser(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereEqualTo("username", "deneme");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, com.parse.ParseException e) {

                if (objects.size() > 0) {
                    for (final ParseObject object : objects) {
                        assertEquals("User added!, Test passed.", 8, objects.size());
                    }
                }
            }
        });
    }
}
