
package utilitez;


/**
 * Class Checks
 * @autor MotYim
 */

public class Checks {
    /**
     * check that string not empty or null
     * @param str
     * @return  - true if string is correct <br>
     *           - false if string incorrect
     */
    
    public static boolean checkStringEmpty(String str ){
         if (str == null || str.trim().length() == 0 || str.trim()=="") {
           return false;
        }
         return true ;
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
