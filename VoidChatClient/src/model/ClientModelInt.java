package model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientModelInt extends Remote{
    
    void notify(String message , int type)  throws RemoteException;
    
    void notifyStatus(String username , String status) throws RemoteException;
    
   // void reciveMsg(String msg)  throws RemoteException;
     void reciveMsg(Message message)  throws RemoteException;
     
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers)  throws RemoteException;
   
    /**
     * receive Announcement from server
     * @param message 
     * @throws java.rmi.RemoteException 
     */
    void receiveAnnouncement(String message)throws RemoteException;

    
    /**
     * 
     * @param sender
     * @return url location or null if not file choosen
     * @throws RemoteException 
     */
    String getSaveLocation(String sender)throws RemoteException;
    
    /**
    * save file
    * @param path
    * @param extension
    * @param data
    * @param dataLength
    * @throws RemoteException 
    */
    void reciveFile(String path , String extension, byte [] data , int dataLength)throws RemoteException;
    
    /**
     * 
     * @param data
     * @param dataLength 
     */
    void reciveSponser(byte[] data, int dataLength)throws RemoteException;
     void loadErrorServer()throws RemoteException;
}
