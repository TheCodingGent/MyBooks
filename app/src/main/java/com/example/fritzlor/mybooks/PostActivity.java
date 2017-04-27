package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.fritzlor.mybooks.models.Book;
import com.example.fritzlor.mybooks.models.Post;
import com.example.fritzlor.mybooks.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabase;

    EditText etBookTitle, etBookAuthor, etBookDescription, etBookPrice;
    RadioButton rbNew, rbUsed;
    RadioGroup radioGroup;
    Button bPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        etBookTitle = (EditText) findViewById(R.id.etBookTitle);
        etBookAuthor = (EditText) findViewById(R.id.etBookAuthor);
        etBookDescription = (EditText) findViewById(R.id.etBookDescription);
        etBookPrice = (EditText) findViewById(R.id.etBookPrice);

        bPost = (Button) findViewById(R.id.bPost);

        radioGroup = (RadioGroup) findViewById(R.id.rgCondition);

        bPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etBookTitle.getText().toString();
                String author = etBookAuthor.getText().toString();
                String description = etBookDescription.getText().toString();
                String price = etBookPrice.getText().toString();

                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                String condition = (String) radioButton.getText();

                //BASIC VALIDATION
                if(title.length()<=0 || author.length()<=0 || price.length()<=0)
                {
                    Toast.makeText(PostActivity.this,"Please Enter all data correctly",Toast.LENGTH_SHORT).show();
                } else {
                    addPostToDb(title, author,description, condition, price, currentUser.getDisplayName(), currentUser.getEmail());
                    Intent intent = new Intent(PostActivity.this, DashboardActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    public void addPostToDb(String title, String author, String description, String condition, String price, String username, String email){
        Book mBook = new Book(UUID.randomUUID().toString(), title, author, description, condition, price);
        User mUser = new User(username, email);
        Post mPost = new Post(UUID.randomUUID().toString(), mUser, mBook);

        mDatabase.child("posts").child(mPost.postId).setValue(mPost);

    }
}
