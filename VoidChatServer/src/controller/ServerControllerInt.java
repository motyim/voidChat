
package controller;

import java.util.ArrayList;
import model.ClientModelInt;
import model.User;
import utilitez.Notification;


public interface ServerControllerInt extends Notification{
    
    /**
     * save object on hash table in server
     * @param username
     * @param obj 
     * @throws java.rmi.RemoteException 
     */
    void register(String username , ClientModelInt obj);
    /**
     * notify user with status
     * @param reciver
     * @param message
     * @param type 
     */
    @Override
    void notify(String reciver , String message , int type);

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
    
    
    /**
     * 
     * @param username
     * @return User or null if not exsits
     */
    User getUserInfo(String username);
    
    /**
     * Server Send announcement message to all users
     * @param message 
     */
    void sendAnnouncement(String message);
    
}
