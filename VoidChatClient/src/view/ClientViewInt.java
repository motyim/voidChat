
package view;

import java.util.ArrayList;

/**
 * 
 * @author 
 */
public interface ClientViewInt {
    
    boolean signup();
    
    void loadHomePage();
    
    void changeStatus();  
    
    void logout();
    
    void sendRequest();
    
    void notify(String senderName);
    
    void acceptRequest();
    
    void notifyStatus(String username , String status);
    
    void sendMsg();
    
    void reciveMsg(String msg);
    
    void groupMsg();
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
}
