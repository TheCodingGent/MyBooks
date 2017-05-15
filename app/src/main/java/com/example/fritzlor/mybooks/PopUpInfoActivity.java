package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public  class PopUpInfoActivity extends AppCompatActivity {
    TextView tvBookCondition, tvBookDescription,etBookPrice,tvBookTitle, tvBookAuthor;
    String Title, Author,Condition, Description, Price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_info);

        tvBookTitle = (TextView) findViewById(R.id.tvBookT_info);
        tvBookAuthor = (TextView) findViewById(R.id.tvBookA_info);
        tvBookCondition = (TextView) findViewById(R.id.tvBookC_info);
        tvBookDescription = (TextView) findViewById(R.id.tvBookD_info);
        etBookPrice = (TextView) findViewById(R.id.tvBookP_info);

        Intent Info = getIntent();
        Title = Info.getStringExtra("Title");
        Author = Info.getStringExtra("Author");
        Condition = Info.getStringExtra("Condition");
        Description = Info.getStringExtra("Description");
        Price = Info.getStringExtra("Price");

        set_info(Title,Author,Condition,Description,Price);
    }

     void set_info(String Title, String Author, String Condition, String Description, String Price){

        tvBookTitle.setText(Title);
        tvBookAuthor.setText(Author);
        tvBookCondition.setText(Condition);
        tvBookDescription.setText(Description);
        etBookPrice.setText(Price);
    }

}
