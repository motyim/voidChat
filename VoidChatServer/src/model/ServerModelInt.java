package model;


import java.rmi.*;
import java.util.ArrayList;

public interface ServerModelInt extends Remote {
    
    /**
     * 
     * @param user
     * @return 
     * @throws java.rmi.RemoteException 
     */
    boolean signup(User user)throws RemoteException;
    
    /**
     * load info from database and change status
     * @param username
     * @param password
     * @return User object if null -> false
     * @throws java.rmi.RemoteException
     */
    User signin(String username , String password)throws RemoteException;
    
    /**
     * save object on hash table in server
     * @param username
     * @param obj 
     * @throws java.rmi.RemoteException 
     */
    void register(String username , ClientModelInt obj) throws RemoteException;
    
    /**
     * 
     * @param username 
     * @throws java.rmi.RemoteException 
     */
    void unregister(String username) throws RemoteException;
    
    /**
     * 
     * @param userName user 
     * @return array list of friends
     * @throws java.rmi.RemoteException
     */
    ArrayList<User> getContacts(String userName) throws RemoteException;
    
   /**
     * send friend request to other user
     * @param senderName ana
     * @param reciverName bt3o  
     * @throws java.rmi.RemoteException  
     */
    void sendRequest(String senderName, String reciverName) throws RemoteException;
    
    /**
     * 
     * @param username
     * @return null if no new request
     * @throws java.rmi.RemoteException
     */
    ArrayList<String> checkRequest(String username) throws RemoteException;
    
    /**
     * save Friend in table relation 
     * and remove row from request table
     * @param senderName bta3e
     * @param reciverName bt3o  
     * @throws java.rmi.RemoteException  
     */
    void acceptRequest(String senderName , String reciverName ) throws RemoteException;
    
    /**
     * notify form friend request
     * @param senderName
     * @param reciverName 
     * @throws java.rmi.RemoteException 
     */
    void notify(String senderName , String reciverName) throws RemoteException;

    
    /**
     * will use private method to get my friends 
     * compare my friends with online friends Right now ..
     * then send notifation to them .
     * @param username
     * @param status
     * @throws java.rmi.RemoteException
     * 
     */
    void changeStatus(String username,String status) throws RemoteException;
    
    /**
     * if false handel disable send btn 
     * if true contiune flow 
     * @param reciver
     * @param msg
     * @return  
     * @throws java.rmi.RemoteException  
     */
    boolean sendMsg(String reciver , String msg) throws RemoteException;
    
    
    void groupMsg(String msg , ArrayList<String> groupChatUsers) throws RemoteException;
    
    //TODO : remove this method 
    void displayStatus() throws RemoteException;
}
