package com.example.abhishekshukla.shopapp;

/**
 * Created by abhishekshukla on 20/4/15.
 */
import com.example.abhishekshukla.shopapp.dto.Address;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AddressManager {
    private Set<Address> addressSet = new LinkedHashSet<Address>();

    private AddressManager() {
        Address address1 = new Address();
        address1.setName("Abhishek");
        address1.setPincode("560017");
        address1.setAddress("401 Turen Residency 1st cross 4th main vinayaknagar old air port road");
        address1.setCity("Bangalore");
        address1.setState("Karnataka");
        address1.setMobileNumber("7760114433");
        address1.setTown("Bagalore");

        Address address2 = new Address();
        address2.setName("Nitesh");
        address2.setPincode("560017");
        address2.setAddress("401 Turen Residency 1st cross 4th main vinayaknagar old air port road");
        address2.setCity("Bangalore");
        address2.setState("Karnataka");
        address2.setMobileNumber("7760114433");
        address2.setTown("Bagalore");


        Address address3 = new Address();
        address3.setName("Nishant");
        address3.setPincode("560017");
        address3.setAddress("401 Turen Residency 1st cross 4th main vinayaknagar old air port road");
        address3.setCity("Bangalore");
        address3.setState("Karnataka");
        address3.setMobileNumber("7760114433");
        address3.setTown("Bagalore");

        addressSet.add(address1);
        addressSet.add(address2);
        addressSet.add(address3);
    }

    private final static AddressManager addressManager = new AddressManager();

    public static AddressManager getInstance() {
            return addressManager;
    }

    public void addAddress(Address address)
    {
        addressSet.add(address);
    }

    public Set<Address> getAllAddress()
    {
        return addressSet;
    }
}
