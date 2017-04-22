package com.example.fritzlor.mybooks.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yasser.ghamlouch on 2017-04-20.
 */
@IgnoreExtraProperties
public class Book {
    public String bookid;
    public String title;
    public String author;
    public String condition;
    public String description;
    public String price;

    public Book() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Book(String bookid, String title,  String author, String description, String condition, String price) {
        this.bookid = bookid;
        this.title = title;
        this.author = author;
        this.description = description;
        this.condition = condition;
        this.price = price;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("bookid", bookid);
        result.put("title", title);
        result.put("author", author);
        result.put("description", description);
        result.put("condition", condition);

        return result;
    }
}
