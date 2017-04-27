package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fritzlor.mybooks.models.Post;
import com.example.fritzlor.mybooks.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    final static String TAG = "DASHBOARD_ACTIVITY";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;

    private Button bPostDash;
    private ListView lvBooks;

    private List<String> mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts");


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        bPostDash = (Button)findViewById(R.id.bPostDash);
        lvBooks = (ListView)findViewById(R.id.lvBooks);

        // Instanciating an array list
        mPosts = new ArrayList<>();

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> mListAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mPosts);

        lvBooks.setAdapter(mListAdapter);

        bPostDash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    addUserToDb(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());

                    TextView textView = (TextView) findViewById(R.id.tvWelcomeText);
                    textView.append(currentUser.getDisplayName());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    protected void addUserToDb (String userId, String username, String email){
        User user = new User(username, email);
        mDatabase.child("users").child(userId).setValue(user);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    mPosts.add(post.book.title);
                    ((BaseAdapter)lvBooks.getAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(DashboardActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }
    }
}
