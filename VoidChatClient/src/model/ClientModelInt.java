package model;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ClientModelInt extends Remote{
    
    void notify(String senderName)  throws RemoteException;
    
    void notifyStatus(String username , String status) throws RemoteException;
    
    void reciveMsg(String msg)  throws RemoteException;
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers)  throws RemoteException;
    
    
}
