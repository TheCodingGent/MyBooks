package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class DashboardActivity extends AppCompatActivity {

    final static String TAG = "DASHBOARD_ACTIVITY";
    String[] t1={"video1","video2","video1","video2","video1","video2","video1","video2"};
    String[] d1={"lesson1","lesson2","lesson1","lesson2","lesson1","lesson2","lesson1","lesson2"};
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;

    private Button bPostDash;
    private ListView lvBooks;

    private ArrayList<String> mPosts;
    private ArrayList<String> aPosts;

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
        aPosts = new ArrayList<>();

        lvBooks.setAdapter(new PostAdapterActivity(mPosts,aPosts));

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
                    aPosts.add(post.book.author);
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




    public class PostAdapterActivity extends BaseAdapter{

        ArrayList<String> Title, Author;

        public PostAdapterActivity(ArrayList<String> title, ArrayList<String> author) {
            Title = title;
            Author = author;

        }

        public int getCount() {
            // TODO Auto-generated method stub
            return Title.size();
        }
        public Object getTitle() {
            // TODO Auto-generated method stub
            return Title;
        }
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = getLayoutInflater();
                View row;
                row = inflater.inflate(R.layout.activity_poast_adapter, parent, false);
                TextView tvtitle, tvauthor;
                tvtitle = (TextView) row.findViewById(R.id.tvTitleADPT);
                tvauthor = (TextView) row.findViewById(R.id.tvAuthorADPT);
                tvtitle.setText(Title.get(position));
                tvauthor.setText(" By: "+ Author.get(position));

            return (row);
        }

    }

}
