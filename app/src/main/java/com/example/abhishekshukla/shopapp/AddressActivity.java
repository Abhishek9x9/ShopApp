package com.example.abhishekshukla.shopapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import com.example.abhishekshukla.shopapp.dto.Address;
import com.example.abhishekshukla.shopapp.dto.Product;
import com.example.abhishekshukla.shopapp.util.ImageLoader;
import com.example.abhishekshukla.shopapp.util.ImageSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishekshukla on 20/4/15.
 */
public class AddressActivity extends Activity {

    private RadioGroup radioGroup;
    private List<RadioButton> allRadioButtons;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.address_layout);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Button newAddressButton = (Button) findViewById(R.id.add_new_address_button);
        Button proceedToPaymentButton = (Button) findViewById(R.id.payment_button);

        newAddressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewAddress.class);
                view.getContext().startActivity(intent);
            }
        });

        proceedToPaymentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserCart.getInstance().cleanCart();
                finish();
            }
        });
        setUpAddress();
    }

    private void setUpAddress()
    {
        radioGroup.removeAllViews();
        boolean st = false;
        allRadioButtons = new ArrayList<>();
        int index =  0;
        for(Address address : AddressManager.getInstance().getAllAddress()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View inflatedLayout = inflater.inflate(R.layout.address_card, null, false);
            TextView nameTextView = (TextView) inflatedLayout.findViewById(R.id.address_card_name);
            TextView addressLine1TextView = (TextView) inflatedLayout.findViewById(R.id.address_card_address_line_1);
            TextView addressLine2TextView = (TextView) inflatedLayout.findViewById(R.id.address_card_address_line_2);
            TextView addressLine3TextView = (TextView) inflatedLayout.findViewById(R.id.address_card_address_line_3);
            nameTextView.setText(address.getName());
            addressLine1TextView.setText(address.getAddress());
            addressLine2TextView.setText(address.getCity() + ": " + address.getPincode() + "\n"  + address.getState());
            addressLine3TextView.setText("Mobile Number : " + address.getMobileNumber());

            RadioButton radioButton = (RadioButton)inflatedLayout.findViewById(R.id.address_selection);
            allRadioButtons.add(radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    for(RadioButton radioButton : allRadioButtons)
                    {
                        if(!radioButton.equals((RadioButton)v))
                        {
                            radioButton.setChecked(false);
                        }
                    }
                }
            });
            if(!st)
            {
                st = true;
                radioButton.setChecked(true);
            }
            radioGroup.addView(inflatedLayout, index);
            index++;
        }
    }

    @Override
    protected  void  onResume () {
        super.onResume();
        setUpAddress();
    }
}
