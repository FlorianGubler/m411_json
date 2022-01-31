package com.company;

public class RandomUserContact {
    public String street;
    public String city;
    public String state;
    public String country;
    public String postcode;
    public String email;
    public String phone;

    //Default Constructor for JSON Mapping
    public RandomUserContact(){}

    public RandomUserContact(String street, String city, String state, String country, String postcode, String email, String phone){
        this.street = street; this.city = city; this.state = state; this.country = country; this.postcode = postcode; this.email = email; this.phone = phone;
    }
}
