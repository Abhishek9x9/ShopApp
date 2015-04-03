package com.example.abhishekshukla.shopapp.util;

import java.util.Collection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.abhishekshukla.shopapp.R;

public class Util {
    /**
     * <code>true</code> if s is <code>null</code> or empty
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static<E> boolean isEmpty(final Collection<E> c){
        return c == null || c.size() == 0;
    }

    public static boolean isEmpty(final byte[] b){
        return b == null || b.length == 0;
    }

    public static boolean isEmpty(final Object[] o){
        return o == null || o.length == 0;
    }
    
    public static String join(final String[] strings, final String separator){
        final StringBuffer b = new StringBuffer();
        join(strings,b,separator);
        return b.toString();
    }

    public static void join(final String[] strings, final StringBuffer buffer, final String separator){
        for(int i = 0; i < strings.length; i++){
            buffer.append(strings[i]);
            if(i+1 < strings.length){
                buffer.append(separator);
            }
        }
    }
    
    public static void showWarningDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
               .setCancelable(false)
               .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                   }
               });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
