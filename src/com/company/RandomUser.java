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
    public String Gender;
    public String Firstname;
    public String Lastname;

    public static String url = "https://randomuser.me/api/";

    public RandomUser(String id, String gender, String firstname, String lastname){
        this.ID = id; this.Gender = gender; this.Firstname = firstname; this.Lastname = lastname;
    }

    //Default Constructor for JSON Mapper
    public RandomUser(){}

    public void printOut(){
        System.out.println("-- Random User Data --");
        System.out.println("Gender: " + this.Gender);
        System.out.println("Firstname: " + this.Firstname);
        System.out.println("Lastname: " + this.Lastname);
        System.out.println("-> See Raw Data: " + url + "?format=pretty&seed=" + this.ID);
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
        }
        System.out.println("User stored in local File '" + Filepath + "' successfully");
    }

    public static RandomUser getNewUser(){
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
            System.out.println("Connection Failure, please try again later");
            return null;
        }

        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonparsed = objectMapper.readTree(response.body());
            JsonNode userresults = jsonparsed.get("results").get(0);

            return new RandomUser(jsonparsed.get("info").get("seed").asText(), userresults.get("gender").asText(), userresults.get("name").get("first").asText(), userresults.get("name").get("last").asText());
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
