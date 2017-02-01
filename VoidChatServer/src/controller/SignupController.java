
package controller;

import utilitez.Checks;
import utilitez.SHA;

/**
 * Class SignupController
 * @autor MotYim
 */
public class SignupController {
    
    private String username;
    private String email;
    private String fName;
    private String lname;
    private String password;
    private String genedr;
    private String country;
    
    public SignupController(){
        
    }

    public void setUsername(String username) throws Exception {
        if(!Checks.checkStringEmpty(username) )
            throw new Exception ("not Valid Username");
        
        if ( !Checks.checkStringLength(username, 3, 20) )
            throw new Exception ("Username must be at least of length 3 and at most 20");
        this.username = username;
    }

    public void setEmail(String email) throws Exception{
        if(!Checks.checkStringEmpty(email))
            throw new Exception ("not Valid Email");
        
        this.email = email;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public void setPassword(String password)throws Exception {
        if(!Checks.checkStringEmpty(email))
            throw new Exception ("not Valid Password");
        if ( !Checks.checkStringLength(password, 8, 50) )
            throw new Exception ("Password must be at least of length 8 ");
        this.password = password;
    }

    public void setGenedr(String genedr) {
        this.genedr = genedr;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    
    public boolean singUp(){
        password = SHA.encrypt(password);
        //TODO deal with model
        return true ;
    }
    
    
    
}
