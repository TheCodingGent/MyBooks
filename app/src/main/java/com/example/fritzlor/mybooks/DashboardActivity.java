package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;

    public ArrayList<String> Title, Author, Condition, Price, Description;
    private Button bPostDash;
    private ListView lvBooks;

    private ArrayList<String> mPosts,dPosts,cPosts,pPosts;
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
        pPosts = new ArrayList<>();
        cPosts = new ArrayList<>();
        dPosts = new ArrayList<>();

        lvBooks.setAdapter(new PostAdapterActivity(mPosts,aPosts,cPosts,dPosts,pPosts));
        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                Intent Info = new Intent(DashboardActivity.this, PopUpInfoActivity.class);
               /* Log.d(Title.get(position), "TITLE");  THESE WERE MEANT FOR DEBUGGING
                Log.d(Author.get(position), "AUTHOR");
                Log.d(Condition.get(position), "CONDITION");
                Log.d(Description.get(position), "DESCRIPTION");
                Log.d(Price.get(position), "PRICE");*/
                Info.putExtra("Title",Title.get(position));
                Info.putExtra("Author",Author.get(position));
                Info.putExtra("Condition",Condition.get(position));
                Info.putExtra("Description",Description.get(position));
                Info.putExtra("Price",Price.get(position));

                startActivity(Info);
            }
        });

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
                    pPosts.add(post.book.price);
                    dPosts.add(post.book.description);
                    cPosts.add(post.book.condition);
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

        public PostAdapterActivity(ArrayList<String> title, ArrayList<String> author ,
                                   ArrayList<String> condition, ArrayList<String> description,
                                   ArrayList<String> price ) {
            Title = title;
            Author = author;
            Condition = condition;
            Description = description;
            Price = price;

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
            row = inflater.inflate(R.layout.activity_post_adapter, parent, false);
            TextView tvtitle, tvauthor;
            tvtitle = (TextView) row.findViewById(R.id.tvTitleADPT);
            tvauthor = (TextView) row.findViewById(R.id.tvAuthorADPT);
            tvtitle.setText(Title.get(position));
            tvauthor.setText(" By: " + Author.get(position));

            return (row);
        }
    }

}
