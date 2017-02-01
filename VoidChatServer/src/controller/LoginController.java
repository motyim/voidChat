
package controller;

import utilitez.SHA;
import utilitez.Checks;

/**
 * Class LoginController
 * @autor MotYim
 */

public class LoginController {
    
    String username ; 
    String password ; 

    public LoginController(String username, String password) throws Exception {
        //check username  & password 
        if(!(Checks.checkStringEmpty(password) || Checks.checkStringEmpty(username)))
            throw new Exception("Please Enter Invalid Username And Password");
        
        this.username = username;
        this.password = password;
    }
    
    
    public boolean singIn() throws Exception  {
        //encrypt password 
        String encryptedPassword =  SHA.encrypt(password);
        
        //TODO connect Model 
        
        return true; 
    }
    
    
    
    
    
    
}
