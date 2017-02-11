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
    
}
