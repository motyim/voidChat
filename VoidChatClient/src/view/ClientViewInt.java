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

    void sendRequest();

    void notify(String senderName);

    void acceptRequest();

    void notifyStatus(String username, String status);

    void sendMsg();

    void reciveMsg(String msg);

    void groupMsg();
<<<<<<< HEAD
    
    void reciveMsgGroup(String msg ,ArrayList<String> groupChatUsers);
    
    ArrayList<User> getContacts();
    
    ArrayList<String> checkRequest();
=======

    void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers);
>>>>>>> 3e0c40bae104758bc65a2673b3af553f7bb302b6
}
