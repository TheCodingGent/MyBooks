package com.example.fritzlor.mybooks;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fritzlor.mybooks.DataBase_Activities.RegistrationHelper;

/**
 * Created by Fritzlor on 2017-04-16.
 */

public class Register  extends AppCompatActivity {

    //PHP SCRIPT URL
    final static String urlAddress="http://fritzlorauguste.000webhostapp.com/Register.php/";
    //EDITTEXTS
    EditText etName,etEmail,etPassword,etConfirmPassword;
    Button bRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        etName= (EditText) findViewById(R.id.etName);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etPassword= (EditText) findViewById(R.id.etPassword);
        etConfirmPassword= (EditText) findViewById(R.id.etConfirmPassword);
        bRegister= (Button) findViewById(R.id.bRegister);


        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname=etName.getText().toString();
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                String confirmPwd=etConfirmPassword.getText().toString();

//BASIC VALIDATION
                if((fullname.length()<=0 || fullname==null) || (email.length()<=0||email==null)
                        ||( password.length()<=0 || password==null))
                {
                    Toast.makeText(Register.this,"Please Enter all data correctly",Toast.LENGTH_SHORT).show();
                }
                else
                if( ! password.equals(confirmPwd))
                {
                    Toast.makeText(Register.this,"Password does not match",Toast.LENGTH_SHORT).show();

                }else {
                    new RegistrationHelper(Register.this,urlAddress,etName,etEmail,etPassword).execute();
                    etConfirmPassword.setText("");
                }
            }
        });

       // fab.setOnClickListener(new View.OnClickListener() {
         //   @Override
           // public void onClick(View view) {
             //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
          //  }
        //});
 //   }


}


}
