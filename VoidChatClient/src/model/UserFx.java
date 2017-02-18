/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Merna
 */

public class UserFx implements Serializable{

    private SimpleStringProperty username;
    private SimpleStringProperty email;
    private SimpleStringProperty fname;
    private SimpleStringProperty lname;
    private SimpleStringProperty password;
    private SimpleStringProperty gender;
    private SimpleStringProperty country;
    private SimpleStringProperty status;

    public UserFx(String username, String email, String fname, String lname, String gender, String country) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.gender = new SimpleStringProperty(gender);
        this.country = new SimpleStringProperty(country);
    }

    public UserFx(String email, String password) {
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
    }
    
    

    public UserFx(String username, String email, String fname, String lname, String password, String gender, String country, String status) {
        
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.fname = new SimpleStringProperty(fname);
        this.lname = new SimpleStringProperty(lname);
        this.password = new SimpleStringProperty(password);
        this.gender = new SimpleStringProperty(gender);
        this.country = new SimpleStringProperty(country);
        this.status = new SimpleStringProperty(status);
    }

   

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFname() {
        return fname.get();
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public String getLname() {
        return lname.get();
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
    
}
