
package utilitez;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Checks {
    
    private final static  Pattern EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-.]+([A-Za-z0-9-_.]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$");
    private final static  Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_.]{3,20}");
    private final static  Pattern NAME_PATTERN = Pattern.compile("[a-zA-Z]{3,10}");
    private final static  Pattern IP_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
   
    private static Matcher match;

    private Checks() {
    }
    
    
  
    /**
     * check that email address not empty or correct
     * @param email
     * @return - true if email valid <br> 
     *          - false if not valid 
     */
    public static boolean checkEmail(String email){
         match = EMAIL_PATTERN.matcher(email);
         return !email.trim().equals("") && match.matches();
    }
    
    /**
     * check that username not empty or correct
     * @param username
     * @return - true if username valid <br> 
     *          - false if not valid 
     */
    public static boolean checkUserName(String username){
        match = USERNAME_PATTERN.matcher(username);
        return   checkStringEmpty(username) && match.matches();
    }
    
    
    /**
     * check that name not empty or correct
     * @param name
     * @return - true if name valid <br> 
     *          - false if not valid 
     */
    public static boolean checkName(String name){
        match = NAME_PATTERN.matcher(name);
        return   checkStringEmpty(name) && match.matches();
    }
    
    /**
     * check if ip is valid ip v4
     * @param ip
     * @return -ture if ip is valid <br>
     *          -false if not
     */
    public static boolean checkIP(String ip){
        match = IP_PATTERN.matcher(ip);
        return match.matches();
    }
    
    
    /**
     * check that string not empty or null
     * @param str
     * @return  - true if string is correct <br>
     *           - false if string incorrect
     */
    public static boolean checkStringEmpty(String str ){
         return !(str == null || str.trim().length() == 0 || "".equals(str.trim())) ;
    }
    
    
    /**
     * check string length with in min & max value length allowed
     * @param str string to be checked
     * @param min length accepted for string
     * @param max length accepted for string
     * @return - true if string with in range <br>
     *          - false if string not with in range
     */
    public static boolean checkStringLength(String str , int min , int max){
        return !(str.length() > max || str.length() < min );
    }
}
