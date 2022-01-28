package com.company;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            //Show Menu
            System.out.println("\n-- Menu JSON Project v01 --");
            System.out.println("1 - Get New Random User");
            System.out.println("2 - Load Random User from Local");
            System.out.println("3 - Exit");
            System.out.print("INP - $ ");
            String inp = sc.nextLine();
            switch (inp.toLowerCase(Locale.ROOT)){
                case "1": loadNewUser(); break;
                case "2": loadUserLocal(); break;
                case "3": System.exit(0); break;
                default: System.out.println("Invalid Input, please try again");
            }
        }
    }

    public static void loadNewUser(){
        RandomUser newuser = RandomUser.getNewUser();
        if(newuser != null){
            newuser.printOut();
            Scanner sc = new Scanner(System.in);
            boolean checkanswer = true;
            while(checkanswer){
                System.out.print("\nStore Customer Object in File? (y/n) ");
                String answer = sc.nextLine();
                switch (answer.toLowerCase(Locale.ROOT)){
                    case "y":
                        newuser.storeLocal(); checkanswer = false; break;
                    case "n":
                        checkanswer = false; break;
                    default:
                        System.out.println("Invalid Input, please try again");
                }
            }
        }
    }

    public static void loadUserLocal(){
        RandomUser loadeduser = RandomUser.loadUserLocal();
        if(loadeduser != null){
            loadeduser.printOut();
        }
    }
}
