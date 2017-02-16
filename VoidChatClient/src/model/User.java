package model;

import java.io.Serializable;

public class User implements Serializable{

    private String username;
    private String email;
    private String fname;
    private String lname;
    private String password;
    private String gender;
    private String country;
    private String status;

    public User(String username, String email, String fname, String lname, String password, String gender, String country) {
        this.username = username;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.gender = gender;
        this.country = country;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    public User(String username, String fname, String lname,  String gender, String country) {
        
        this.username = username;
        this.fname = fname;
        this.lname = lname;  
        this.gender = gender;
        this.country = country;
  
    }

    public User(String username, String email, String fname, String lname, String password, String gender, String country, String status) {
        
        this.username = username;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.password = password;
        this.gender = gender;
        this.country = country;
        this.status = status;
    }

   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}