
package controller;

import java.util.ArrayList;
import model.ClientModelInt;


public interface ServerControllerInt {
    
    /**
     * save object on hash table in server
     * @param username
     * @param obj 
     * @throws java.rmi.RemoteException 
     */
    void register(String username , ClientModelInt obj);
    
    void notify(String SenderName , String reciverName);

    void notifyStatus(String username , String status ,ArrayList<String> friends);
    
      /**
     * if false handel disable send btn 
     * if true contiune flow 
     * @param reciver
     * @param msg
     * @return  
     */
    boolean sendMsg(String reciver , String msg);
    
    /**
     * check for user group online user and send Msg 
     * for them .. fokkak mn offline now 
     * @param msg
     * @param groupChatUsers 
     */
    //TODO : handle when user left group
    void groupMsg(String msg , ArrayList<String> groupChatUsers);
    void startServer();
    void stopServer();
    
}
