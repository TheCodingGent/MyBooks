package com.example.fritzlor.mybooks.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasser.ghamlouch on 2017-04-20.
 */

@IgnoreExtraProperties
public class Post {
    public String postId;
    public User user;
    public Book book;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String postId, User user, Book book) {
        this.postId = postId;
        this.user = user;
        this.book = book;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", book.title);
        result.put("author", book.author);
        result.put("condition", book.condition);
        result.put("description", book.description);
        result.put("user", user);

        return result;
    }
}
