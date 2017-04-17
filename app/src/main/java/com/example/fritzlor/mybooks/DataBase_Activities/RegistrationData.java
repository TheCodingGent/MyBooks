package com.example.fritzlor.mybooks.DataBase_Activities;


import com.example.fritzlor.mybooks.User;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class RegistrationData {


    User user;

    public RegistrationData(User user) {
        this.user = user;
    }

    public String packRegistrationData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer jsonData=new StringBuffer();

        try
        {
            jo.put("NAME",user.getFullname());
            jo.put("EMAIL",user.getEmail());
            jo.put("PASSWORD",user.getPassword());

            Boolean isFirstValue=true;
            Iterator it=jo.keys();

            do {
                String key=it.next().toString();
                String value=jo.get(key).toString();

                if(isFirstValue)
                {
                    isFirstValue=false;
                }else {
                    jsonData.append("&");
                }

                jsonData.append(URLEncoder.encode(key,"UTF-8"));
                jsonData.append("=");
                jsonData.append(URLEncoder.encode(value,"UTF-8"));


            }while (it.hasNext());

            return jsonData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return ErrorTracker.REGISTRATION_DATA_ERROR;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ErrorTracker.REGISTRATION_DATA_ENCODING_ERROR;
        }

    }
}
