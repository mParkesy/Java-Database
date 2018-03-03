/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqltest;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author 100116544
 */
public class User {

    private int id;
    private String username;
    private String firstname;
    private String lastname;
    private String gender;
    private String postcode;
    private String nationality;
    private String email;
    private double height;
    private double weight;
    private double bmi;
    private int calories;
    private Date dob;   

    private final Connection con;

    /**
     * A constructor to create an object from an existing user in the database
     *
     * @param id
     * @throws Exception
     */
    public User(int id) throws Exception {
        this.con = Database.getConnection();
        String sql = "SELECT *, COUNT(*) FROM user WHERE userID =?";
        PreparedStatement st = con.prepareStatement(sql);
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            if (rs.getInt("COUNT(*)") == 0) {
                throw new Exception("No user exists with that User ID");
            }
            this.id = id;
            this.username = rs.getString("username");
            this.firstname = rs.getString("firstname");
            this.lastname = rs.getString("lastname");
            this.gender = rs.getString("gender");
            this.dob = rs.getDate("dob");
            this.postcode = rs.getString("postcode");
            this.nationality = rs.getString("nationality");
            this.email = rs.getString("email");
            this.height = rs.getFloat("height");
            this.weight = rs.getFloat("weight");
            this.bmi = rs.getFloat("bmi");
            this.calories = rs.getInt("calories");
        }
    }

    /**
     *
     * @param username
     * @param password
     * @param firstname
     * @param lastname
     * @param gender
     * @param dob
     * @param postcode
     * @param nationality
     * @param email
     * @param height
     * @param weight
     * @param bmi
     * @param calories
     * @throws java.lang.Exception
     */
    public User(String username, String password, String firstname,
            String lastname, String gender, Date dob, String postcode, 
            String nationality, String email, double height, double weight, 
            double bmi, int calories)
            throws Exception {
        this.con = Database.getConnection();
        try{
            

            String dbPassword = "";
            try {
                dbPassword = Database.passwordDigest(password);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }

            String sql = "INSERT INTO `user`(username, password, firstname, "
                    + "lastname, gender, dob, postcode, nationality, email, "
                    + "height, weight, bmi, calories) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement st = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, username);
            st.setString(2, dbPassword);
            st.setString(3, firstname);
            st.setString(4, lastname);
            st.setString(5, gender);
            java.sql.Date date = new java.sql.Date(dob.getTime());
            st.setDate(6, date);
            st.setString(7, postcode);
            st.setString(8, nationality);
            st.setString(9, email);
            st.setDouble(10, height);
            st.setDouble(11, weight);
            st.setDouble(12, bmi);
            st.setInt(13, calories);

            st.executeUpdate();

            // get the auto incremented id created by the database, construct user
            try (ResultSet key = st.getGeneratedKeys()) {
                if (key.next()) {
                    this.id = key.getInt(1);
                    this.username = username;
                    this.firstname = firstname;
                    this.lastname = lastname;
                    this.gender = gender;
                    this.dob = dob;
                    this.postcode = postcode;
                    this.nationality = nationality;
                    this.email = email;
                    this.height = height;
                    this.weight = weight;
                    this.bmi = bmi;
                    this.calories = calories;
                } else {
                    throw new SQLException("No ID found, User not created");
                }
            }
        } catch (SQLException ex){
            System.out.println("Duplicate database entry");
        }
    }

    public boolean changePasswordBefore(String username, String email, 
            String newPassword, String oldPassword) throws Exception {
        
        try {
            String dbPassword = "";
            try {
                dbPassword = Database.passwordDigest(oldPassword);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }             

            String sql = "SELECT *, COUNT(*) FROM user WHERE password = ? "
                    + "AND username = ? AND email = ?";
            PreparedStatement st = this.con.prepareStatement(sql);
            st.setString(1, dbPassword);
            st.setString(2, username);
            st.setString(3, email);

            ResultSet result = st.executeQuery();
            int userID = result.getInt("userID");

            while(result.next()){
                if(result.getInt("COUNT(*)") == 0){
                    throw new Exception("No user found with those credentials.");
                }
                try {
                    dbPassword = Database.passwordDigest(newPassword);
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                } 
                
                try {
                    sql = "UPDATE user SET password = ? WHERE userID = ?";
                    st = this.con.prepareStatement(sql);
                    st.setString(1, dbPassword);
                    st.setInt(2, userID);
                    st.executeUpdate();
                    return true; 
                } catch(SQLException ex){
                    System.out.println(ex.toString());
                    return false;
                }
            }
        } catch(Exception ex){
            System.out.println(ex.toString());
            return false;
        }
        return false;
    }
    
    public boolean changePasswordAfter(String newPassword){
        try{
            String dbPassword = "";
            try {
                dbPassword = Database.passwordDigest(newPassword);
            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
            }   
            
            String sql = "UPDATE user SET password = ? WHERE userID = ?";
            PreparedStatement st = this.con.prepareStatement(sql);
            st.setString(1, dbPassword);
            st.setInt(2, this.id);
            st.executeUpdate();
            return true;
        } catch(SQLException ex){
            System.out.println(ex.toString());
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getNationality() {
        return nationality;
    }

    public String getEmail() {
        return email;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getBmi() {
        return bmi;
    }

    public int getCalories() {
        return calories;
    }

    public Date getDob() {
        return dob;
    }

    public Connection getCon() {
        return con;
    }

    

}
