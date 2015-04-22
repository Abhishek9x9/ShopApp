package com.example.abhishekshukla.shopapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.abhishekshukla.shopapp.R;
import com.example.abhishekshukla.shopapp.dto.Address;

public class NewAddress extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_fill);

        final EditText pincode = (EditText) findViewById(R.id.pincode_edit_text);
        final EditText name = (EditText) findViewById(R.id.name_edit_text);
        final EditText address = (EditText) findViewById(R.id.address_edit_text);
        final EditText town = (EditText) findViewById(R.id.town_edit_text);
        final EditText city = (EditText) findViewById(R.id.city_edit_text);
        final EditText state = (EditText) findViewById(R.id.state_edit_text);
        final EditText mobile = (EditText) findViewById(R.id.mobile_edit_text);

        Button saveButton = (Button) findViewById(R.id.address_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(null == pincode.getText() || pincode.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide pincode",Toast.LENGTH_SHORT).show();
                }
                else if(null == name.getText() || name.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide name",Toast.LENGTH_SHORT).show();
                }
                else if(null == address.getText() || address.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide address",Toast.LENGTH_SHORT).show();
                }
                else if(null == town.getText() || town.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide town",Toast.LENGTH_SHORT).show();
                }
                else if(null == city.getText() || city.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide city",Toast.LENGTH_SHORT).show();
                }
                else if(null == state.getText() || state.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide state",Toast.LENGTH_SHORT).show();
                }
                else if(null == mobile.getText() || mobile.getText().toString().trim().length() == 0 )
                {
                    Toast.makeText(getApplicationContext(), "Please provide mobile number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                   Address addressCard = new Address();
                   addressCard.setPincode(pincode.getText().toString());
                   addressCard.setName(name.getText().toString());
                   addressCard.setTown(town.getText().toString());
                   addressCard.setMobileNumber(mobile.getText().toString());
                   addressCard.setState(state.getText().toString());
                   addressCard.setCity(city.getText().toString());
                   addressCard.setAddress(address.toString());
                   AddressManager.getInstance().addAddress(addressCard);
                   Toast.makeText(getApplicationContext(), "Address saved",Toast.LENGTH_SHORT).show();
                   finish();
                }
            }
        });
    }
}
