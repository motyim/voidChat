package view;

import java.util.ArrayList;
import model.User;

/**
 *
 * @author MotYim
 */
public interface ClientViewInt {

    boolean signup(User user) throws Exception;

    User signin(String username, String password) throws Exception;

    void loadHomePage();

    void changeStatus();

    void logout();

    
    int sendRequest(String friend,String category);
    

    void notify(String senderName);

    void acceptRequest();

    void notifyStatus(String username, String status);

    void sendMsg();

    void reciveMsg(String msg);

    void groupMsg();
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
    
    ArrayList<User> getContacts();
    
    public void showError(String title, String header, String content) ;
    
    public void showSuccess(String title, String header, String content); 
    
    ArrayList<String> checkRequest();

}
