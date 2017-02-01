package model;

import java.util.ArrayList;

public interface ClientModelInt {
    
    void notify(String senderName);
    
    void notifyStatus(String username , String status);
    
    void reciveMsg(String msg);
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
    
    
}
