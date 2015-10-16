package com.example.abhishekshukla.shopapp.auth;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.view.CustomEditTextView;

public class RegistrationActivity extends AppCompatActivity {

    private SMSHandler smsHandler = new SMSHandler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        final Button buttonView = (Button)findViewById(R.id.otp);
        buttonView.addTextChangedListener(new TextWatcher() {
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        buttonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Context context = getApplicationContext();
                EditText editTextView = (EditText) findViewById(R.id.phone_no);
                final String phoneNo = editTextView.getText().toString();
                smsHandler.sendSMSMessage(context, phoneNo, "OTP is:" + smsHandler.generatePIN());
                try {
                    Thread.sleep(6000);
                }catch (Exception e){

                }
                String otpReceived = smsHandler.readSMS(context);
                EditText receivedOTPView= (EditText)findViewById(R.id.otp_received);
                receivedOTPView.setText(otpReceived);
                receivedOTPView.setVisibility(View.VISIBLE);
            }
        });


    }
}
