package com.example.fritzlor.mybooks.DataBase_Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fritzlor.mybooks.Dashboard;
import com.example.fritzlor.mybooks.User;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

//import User;

/**
 * Created by Oclemy on 6/9/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class RegistrationHelper extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAddress;
    EditText etName, etEmail, etPassword;

    ProgressDialog pd;
    User user;

    public RegistrationHelper(Context c, String urlAddress, EditText...editTexes) {
        this.c = c;
        this.urlAddress = urlAddress;
        this.etName = editTexes[1];
        this.etEmail = editTexes[2];
        this.etPassword = editTexes[3];

        user=new User();
        user.setFullname(etName.getText().toString());
        user.setEmail(etEmail.getText().toString());
        user.setPassword(etPassword.getText().toString());

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Registering your account ...");
        //pd.setMessage("Registering..Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.registerUser();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        pd.dismiss();

        if(response.startsWith("Error"))
        {
            display(response);
        }else
        {
            if(response.startsWith(ErrorTracker.USER_EXISTS))
            {
                display(response);
            }else if(response.startsWith(ErrorTracker.SUCCESS_REG))
            {
                //CLEAR TXTS
                etName.setText("");
                etEmail.setText("");
                etPassword.setText("");

                display(response);
                Intent i=new Intent(c, Dashboard.class);
                c.startActivity(i);

            }
        }

    }

    private void display(String txt)
    {
        Toast.makeText(c,txt,Toast.LENGTH_LONG).show();
    }

    private String registerUser()
    {
        Object connection=Connector.connect(urlAddress);
        if(connection.toString().startsWith("Error"))
        {
            return connection.toString();
        }

        try
        {
            HttpURLConnection con= (HttpURLConnection) connection;
            OutputStream os=new BufferedOutputStream(con.getOutputStream());
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));

            String registrationData=new RegistrationData(user).packRegistrationData();
            if(registrationData.startsWith("Error"))
            {
                return registrationData;
            }

            bw.write(registrationData);
            bw.flush();
            bw.close();
            os.close();

            //GET RESPONSE
            int responseCode=con.getResponseCode();
            if(responseCode==con.HTTP_OK)
            {
                InputStream is=new BufferedInputStream(con.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                String line;
                StringBuffer response=new StringBuffer();

                while ((line=br.readLine()) != null)
                {
                    response.append(line+"\n");
                }

                br.close();
                is.close();

                return response.toString();


            }else {
                return ErrorTracker.RESPONSE_ERROR+String.valueOf(responseCode);
            }


        } catch (IOException e) {
            e.printStackTrace();
            return ErrorTracker.IO_ERROR;
        }
    }
}















