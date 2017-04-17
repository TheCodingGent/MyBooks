package com.example.fritzlor.mybooks.DataBase_Activities;




import com.example.fritzlor.mybooks.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

public class LoginData {


    User user;

    public LoginData(User user) {
        this.user = user;
    }

    public String packLoginData()
    {
        JSONObject jo=new JSONObject();
        StringBuffer packedData=new StringBuffer();

        try
        {
            jo.put("USERNAME",user.getUsernameOrEmail());
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
                    packedData.append("&");
                }

                packedData.append(URLEncoder.encode(key, "UTF-8"));
                packedData.append("=");
                packedData.append(URLEncoder.encode(value, "UTF-8"));


            }while (it.hasNext());


            return packedData.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return ErrorTracker.LOGIN_DATA_ERROR;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ErrorTracker.LOGIN_DATA_ENCODING_ERROR;
        }
    }
}