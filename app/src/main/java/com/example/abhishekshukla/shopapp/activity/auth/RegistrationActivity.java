package com.example.abhishekshukla.shopapp.activity.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.activity.review.CartReviewActivity;
import com.example.abhishekshukla.shopapp.auth.UserAuth;
import com.example.abhishekshukla.shopapp.auth.UserInfo;

import java.util.logging.Logger;

public class RegistrationActivity extends AppCompatActivity {

    private final String OTPSMS = " is your one time password (OTP) as requested. Thanks for verifying your phone number.";
    private SMSHandler smsHandler = new SMSHandler();
    private ProgressBar progressBar;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button buttonView = (Button) findViewById(R.id.otp);

        EditText editText = (EditText) findViewById(R.id.phone_no);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // No entered text so will show hint
                    buttonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    buttonView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                }
                buttonView.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //buttonView.setEnabled(true);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        buttonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Context context = getApplicationContext();
                EditText editTextView = (EditText) findViewById(R.id.phone_no);
                final String phoneNo = editTextView.getText().toString();
                final String pin = smsHandler.generatePIN();
                smsHandler.sendSMSMessage(context, phoneNo, pin + OTPSMS);
                Logger.getAnonymousLogger().info("OPT generated:" + pin);
                buttonView.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6000);
                            final String otpReceived = smsHandler.readSMS(context);
                            Logger.getAnonymousLogger().info("OPT received:" + otpReceived);
                            if (otpReceived.equals(pin)) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        EditText receivedOTPView = (EditText) findViewById(R.id.otp_received);
                                        receivedOTPView.setText(otpReceived);
                                        receivedOTPView.setVisibility(View.VISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);

                                        final ProgressDialog ringProgressDialog = ProgressDialog.show(RegistrationActivity.this, "Please wait", "Verifying phone number...", true);
                                        ringProgressDialog.setCancelable(true);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    // Here you should write your time consuming task...
                                                    // Let the progress ring for 10 seconds...
                                                    Thread.sleep(5000);
                                                } catch (Exception e) {

                                                }
                                                ringProgressDialog.dismiss();
                                                //setting userinfo
                                                UserInfo userInfo = new UserInfo();
                                                userInfo.setPhoneNo(phoneNo);
                                                UserAuth.getInstance().setUserInfo(userInfo);
                                                UserAuth.getInstance().saveUserInfo(getApplicationContext());
                                                //////////////////
                                                Intent intent = new Intent(RegistrationActivity.this, CartReviewActivity.class);
                                                RegistrationActivity.this.startActivity(intent);
                                            }
                                        }).start();
                                    }
                                });

                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        buttonView.setEnabled(true);
                                    }
                                });
                            }

                        } catch (Exception e) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    buttonView.setEnabled(true);
                                }
                            });

                        }

                    }
                }).start();
            }
        });

    }

    public void launchRingDialog(View view) {

    }

}
