package model;

import java.util.ArrayList;

public interface ServerModelInt {
    
    /**
     * 
     * @param user
     * @return 
     */
    boolean signup(User user);
    
    /**
     * load info from database and change status
     * @param username
     * @param password
     * @return User object if null -> false
     */
    User signin(String username , String password);
    
    /**
     * save object on hash table in server
     * @param username
     * @param obj 
     */
    void register(String username , ClientModelInt obj);
    
    /**
     * 
     * @param username 
     */
    void unregister(String username);
    
    /**
     * 
     * @param id user 
     * @return array list of friends
     */
    ArrayList<User> getContacts(int id);
    
    /**
     * send friend request to other user
     * @param id ana
     * @param username bt3o  
     */
    void sendRequest(int id , String reciverName);
    
    /**
     * 
     * @param username
     * @return null if no new request
     */
    ArrayList<String> checkRequest(String username);
    
    /**
     * save Friend in table relation 
     * and remove row from request table
     * @param senderName bta3e
     * @param reciverName bt3o  
     */
    void acceptRequest(String senderName , String reciverName );
    
    /**
     * notify form friend request
     * @param senderName
     * @param reciverName 
     */
    void notify(String senderName , String reciverName);

    
    /**
     * will use private method to get my friends 
     * compare my friends with online friends Right now ..
     * then send notifation to them .
     * @param username
     * @param status
     * 
     */
    void changeStatus(String username,String status);
    
    /**
     * if false handel disable send btn 
     * if true contiune flow 
     * @param reciver
     * @param msg
     * @return  
     */
    boolean sendMsg(String reciver , String msg);
    
    
    void groupMsg(String msg , ArrayList<String> groupChatUsers);
}
