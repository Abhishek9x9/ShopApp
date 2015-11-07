package com.example.abhishekshukla.shopapp.auth;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by nishantgupta on 7/11/15.
 */
public class UserAuth {

    private static UserAuth _instance = new UserAuth();
    private UserAuth(){
        //
    }

    public static UserAuth getInstance(){
        return _instance;
    }
    private static final String userInfoFileName = "user_info";
    private static Gson gson = new Gson();
    @Setter
    private UserInfo userInfo = null;

    public void saveUserInfo(Context context){
        Log.v(UserAuth.class.getSimpleName() + "@saveUserInfo", userInfo.toString());
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(userInfoFileName, Context.MODE_WORLD_READABLE);
            outputStream.write(gson.toJson(userInfo).getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isUserLoggedIn(Context context) {
        if(this.userInfo == null){
            StringBuffer datax = new StringBuffer("");
            try {
                FileInputStream fIn = context.openFileInput(userInfoFileName) ;
                InputStreamReader isr = new InputStreamReader ( fIn ) ;
                BufferedReader buffreader = new BufferedReader ( isr ) ;

                String readString = buffreader.readLine ( ) ;
                while ( readString != null ) {
                    datax.append(readString);
                    readString = buffreader.readLine ( ) ;
                }
                isr.close ( ) ;
            } catch ( IOException ioe ) {
                ioe.printStackTrace ( ) ;
            }
            UserInfo userInfo = gson.fromJson(datax.toString(), UserInfo.class);
            this.userInfo = userInfo;
        }
        if(userInfo != null) {
            Log.v(UserAuth.class.getSimpleName() + "@isUserLoggedIn", userInfo.toString());
            return userInfo.getPhoneNo() != null && !userInfo.getPhoneNo().equals("");
        }else{
            return false;
        }
    }
}
