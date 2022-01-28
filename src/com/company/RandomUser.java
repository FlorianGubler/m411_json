package com.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RandomUser {
    public String Gender;
    public String Firstname;
    public String Lastname;

    public RandomUser(String gender, String firstname, String lastname){
        this.Gender = gender; this.Firstname = firstname; this.Lastname = lastname;
    }

    public RandomUser(){}

    public void printOut(){
        System.out.println("-- Random User Data --");
        System.out.println("Gender: " + this.Gender);
        System.out.println("Firstname: " + this.Firstname);
        System.out.println("Lastname: " + this.Lastname);
    }

    public static RandomUser getNewUser(){
        String url = "https://randomuser.me/api/";

        System.out.println("Laoading Data...\n");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Connection Failure, please try again later (Exit)");
            System.exit(0);
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode userresults = objectMapper.readTree(response.body()).get("results").get(0);

            return new RandomUser(userresults.get("gender").asText(), userresults.get("name").get("first").asText(), userresults.get("name").get("last").asText());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Invalid Response (Exit)");
            System.exit(0);
        }
        return null;
    }
}
