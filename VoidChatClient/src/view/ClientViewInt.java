package view;

import java.util.ArrayList;
import model.Message;
import model.User;

/**
 *
 * @author MotYim
 */
public interface ClientViewInt {

    boolean signup(User user) throws Exception;

    User signin(String username, String password) throws Exception;

    void loadHomePage();

    void changeStatus(String status);

    void logout();

    
    int sendRequest(String friend,String category);
     

    void notify(String message , int type);

    boolean acceptRequest(String friend);

    void notifyStatus(String username, String status);

    //void sendMsg(String friendName,String message);
    void sendMsg(Message message);

  //  void reciveMsg(String msg);
        void reciveMsg(Message message);
    void groupMsg();
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
    
    ArrayList<User> getContacts();
    
    public void showError(String title, String header, String content) ;
    
    public void showSuccess(String title, String header, String content); 
    
    ArrayList<String> checkRequest();
    
    User getUserInformation();
    
    /**
     * receive Announcement from server
     * @param message 
     */
    void receiveAnnouncement(String message);
    
    /**
     * refuser friend request
     * @param senderName
     */
    public void ignoreRequest(String senderName);
    void createGroup(String groupName,ArrayList<String> groupMembers);
}
