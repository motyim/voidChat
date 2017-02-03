
package controller;

import java.util.ArrayList;
import model.ClientModelInt;
import model.User;

public interface ClientControllerInt {
    /**
     * validate & create user obj 
     * send to server Model
     * @param user
     * @return 
     */
    boolean signup(User user) throws Exception;
    
    /**
     * validate -> false call show Error in CV 
     *          -> true create user  OBj and ..
     *             send  to SM
     * if get object make this :D 
     * 1- load user home page
     * 2- register to server 
     * 3- getContact
     * 4- check friend requests . 
     * @param username
     * @param password
     * @return User Object 
     * @thows Exception 
     */
    User signin(String username , String password) throws Exception;
    
    /**
     * call loadhomepage in client view 
     * @param client 
     */
    void loadHomePage(User client);
    
    /**
     * call register in SM
     * @param username
     * @param obj 
     */
    void registerToServer(String username , ClientModelInt obj);

    /**
     * call get Contacts in SM uses my id 
     */
    void getContacts();
    
    /**
     * take my username and call checkRequest in SM
     */
    void checkRequest();
   /**
    * 
    * @param status 
    */
   
    void changeStatus(String status); 
    
    /**
     * call unregister from SM
     * handle out form chat mn error w kda
     */
    void logout();
    
    void sendRequest(String reciverName);
    
    void notify(String senderName);
    
    void acceptRequest(String senderName , String reciverName);

    void notifyStatus(String username , String status);
    
    void sendMsg(String reciver , String Msg);
    
    void reciveMsg(String msg);

    void groupMsg(String msg , ArrayList<String> groupChatUsers);
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
}
