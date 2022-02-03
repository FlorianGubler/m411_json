package com.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class RandomUser {
    public String ID;
    public String Title;
    public String Gender;
    public String Firstname;
    public String Lastname;
    public String Picture;
    public RandomUserContact Contact;

    public static String url = "https://randomuser.me/api/";

    public RandomUser(String id, String gender, String title, String firstname, String lastname, String picture, RandomUserContact contact){
        this.ID = id; this.Gender = gender; this.Title = title; this.Firstname = firstname; this.Lastname = lastname; this.Picture = picture; this.Contact = contact;
    }

    //Default Constructor for JSON Mapper
    public RandomUser(){}

    public void printOut(){
        System.out.println("-- Random User Data --");
        System.out.println("Gender: " + this.Gender);
        System.out.println("Title: " + this.Title);
        System.out.println("Firstname: " + this.Firstname);
        System.out.println("Lastname: " + this.Lastname);
        System.out.println("Profile Picture: " + this.Picture);
        System.out.println("\nContact Data of User");
        System.out.println(" - Street: " + this.Contact.street);
        System.out.println(" - City: " + this.Contact.city);
        System.out.println(" - State: " + this.Contact.state);
        System.out.println(" - Country: " + this.Contact.country);
        System.out.println(" - Postcode: " + this.Contact.postcode);
        System.out.println(" - Email: " + this.Contact.email);
        System.out.println(" - Phone: " + this.Contact.phone);

        System.out.println("\n-> See Raw Data: " + url + "?format=pretty&seed=" + this.ID);
    }

    public void storeLocal(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nType in File Name to store Customer: ");
        String Filepath = sc.nextLine();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(Filepath), this);
        } catch (Exception e){
            System.out.println("Something went wrong by writing to File, please try again later");
            return;
        }
        System.out.println("User stored in local File '" + Filepath + "' successfully");
    }

    public static RandomUser getNewUser(){
        System.out.print("Type optional Filters here (Format: key=value&otherkey=othervalue): ");
        String requestBody = new Scanner(System.in).nextLine();
        System.out.println("Loading Data...\n");

        String targeturl = url;
        if(requestBody != ""){
            targeturl = targeturl + "?" + requestBody;
        }

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(targeturl))
                .GET()
                .build();

        HttpResponse<String> response = null;

        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            System.out.println("Connection Failure, please try again later");
            return null;
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonparsed = objectMapper.readTree(response.body());
            JsonNode userresults = jsonparsed.get("results").get(0);

            //Get JSON Values
            String ID = jsonparsed.get("info").get("seed").asText();
            String Gender = userresults.get("gender").asText();
            String Title = userresults.get("name").get("title").asText();
            String Firstname = userresults.get("name").get("first").asText();
            String Lastname = userresults.get("name").get("last").asText();
            String Picture = userresults.get("picture").get("thumbnail").asText();

            String Street = userresults.get("location").get("street").get("name") + userresults.get("location").get("street").get("number").asText();
            String City = userresults.get("location").get("city").asText();
            String State = userresults.get("location").get("state").asText();
            String Country = userresults.get("location").get("country").asText();
            String PostCode = userresults.get("location").get("postcode").asText();
            String Email = userresults.get("email").asText();
            String Phone = userresults.get("phone").asText();

            RandomUserContact Contact = new RandomUserContact(Street, City, State, Country, PostCode, Email, Phone);
            return new RandomUser(ID, Gender, Title, Firstname, Lastname, Picture, Contact);
        } catch (Exception e){
            System.out.println("Invalid Response (Exit)");
            return null;
        }
    }

    public static RandomUser loadUserLocal(){
        Scanner sc = new Scanner(System.in);
        System.out.print("\nType in File Name to store Customer: ");
        String Filepath = sc.nextLine();

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            return objectMapper.readValue(new File(Filepath), RandomUser.class);
        }catch (Exception e){
            System.out.println("Userdata cant be loaded From File '" + Filepath + "'");
            return null;
        }
    }
}
