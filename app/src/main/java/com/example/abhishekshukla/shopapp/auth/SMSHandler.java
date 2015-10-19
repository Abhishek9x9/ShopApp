package com.example.abhishekshukla.shopapp.auth;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Logger;

/**
 * Created by nishantgupta on 16/10/15.
 * Detailed and proper handling: https://mobiforge.com/design-development/sms-messaging-android
 */
public class SMSHandler {

    public static final String INBOX = "content://sms/inbox";

    public String readSMS(Context context) {
        Cursor cursor = context.getContentResolver().query(Uri.parse(INBOX),
                new String[]{"_id", "address", "date", "body"}, "address='+919902957940'", null, "date desc");

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    if(idx == 3){
                        String[] otpSplit = cursor.getString(idx).split(" ");
                        return otpSplit[0];
                    }
                }
                Logger.getAnonymousLogger().info(msgData);
                // use msgData
            } while (cursor.moveToNext());
        } else {
            return "";
        }
        return "";
    }

    public void sendSMSMessage(Context context, String phoneNo, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(context.getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(),
                    "SMS faild, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public String generatePIN()
    {

        //generate a 4 digit integer 1000 <10000
        int randomPIN = (int)(Math.random()*9000)+1000;
        String PINString = String.valueOf(randomPIN);
        return PINString;
    }
}
