package model;


import java.rmi.*;
import java.util.ArrayList;
import utilitez.Notification;

public interface ServerModelInt extends Remote , Notification {
    
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
    int sendRequest(String senderName, String reciverName,String type) throws RemoteException;
    
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
     * @param senderName bta3o
     * @param reciverName bt3e  
     * @return   -true if success <br>
     *            - false if not 
     * @throws java.rmi.RemoteException  
     */
    boolean acceptRequest(String senderName , String reciverName ) throws RemoteException;
    
    /**
     * notify form friend request 
     * @param reciver 
     * @param message 
     * @param type 
     * @throws java.rmi.RemoteException 
     */
    @Override
    void notify(String reciver , String message , int type) throws RemoteException;

    
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
  //  boolean sendMsg(String reciver , String msg) throws RemoteException;
    void sendMsg(Message message)throws RemoteException;
    
    void groupMsg(String msg , ArrayList<String> groupChatUsers) throws RemoteException;
    
    /**
     * refuser friend request
     * @param senderName
     * @param reciverName
     * @throws RemoteException 
     */
    public void ignoreRequest(String senderName,String reciverName) throws RemoteException;
}
