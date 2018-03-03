/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author 100116544
 */
public class Test {
    public static void main(String[] args) throws Exception {
        String username = "parkesy";
        String password = "1234";
        String firstname = "Matt";
        String lastname = "Parkes";
        String gender = "m";
        
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        Date date =  format.parse("10101996");
        
        
        String postcode = "CM54DSA";
        String n = "British";
        String email = "matt.parkes@outlook.com";
        double height = 182.2;
        double weight = 80.2;
        double bmi = 26.1;
        int calorie = 3000;

        User test = new User(username, password, firstname, lastname, gender, date, 
                postcode, n, email, height, weight, bmi, calorie);
        
        System.out.println("User during construction test: " + test.getFirstname());
        
        User test2 = new User(test.getId());
        
        System.out.println("User from database: " + test2.getFirstname());
        
        Scanner reader = new Scanner(System.in); 
        
        System.out.print("Username: ");
        String enteredUsername = reader.nextLine();
        System.out.print("Password: ");
        String enteredPassword = reader.nextLine();
        
        LoginController controller = new LoginController();
        controller.checkLoginDetails(enteredUsername, enteredPassword.toCharArray());
        
        
    }
}
