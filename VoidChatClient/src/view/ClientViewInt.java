package view;

import java.io.File;
import java.util.ArrayList;
import model.ClientModelInt;
import model.Message;
import model.User;
import utilitez.Pair;

/**
 *
 * @author MotYim
 */
public interface ClientViewInt {
    
     /**
     * connect to server by host ip
     * @param host
     * @return 
     */
    public boolean conncetToServer(String host);

    boolean signup(User user) throws Exception;

    User signin(String username, String password) throws Exception;


    void changeStatus(String status);

    void logout();

    int sendRequest(String friend, String category);

    void notify(String message, int type);

    boolean acceptRequest(String friend);

    //void sendMsg(String friendName,String message);
    void sendMsg(Message message);

    //  void reciveMsg(String msg);
    void reciveMsg(Message message);

    ArrayList<User> getContacts();

    public void showError(String title, String header, String content);

    public void showSuccess(String title, String header, String content);

    ArrayList<String> checkRequest();

    
     /**
     * get login user info 
     * @return Login User
     */
    User getUserInformation();

    /**
     * receive Announcement from server
     *
     * @param message
     */
    void receiveAnnouncement(String message);

    /**
     * refuser friend request
     *
     * @param senderName
     */
    public void ignoreRequest(String senderName);

    /**
     * save messages on XML format on file
     *
     * @param file
     * @param messages
     */
    public void saveXMLFile(File file, ArrayList<Message> messages);

    /**
     * make peet-to-peer connection with Client
     *
     * @param Client
     * @return connection
     */
    ClientModelInt getConnection(String Client);

    /**
     *
     * @param sender
     * @param filename
     * @return url location or null if not file choosen
     */
    String getSaveLocation(String sender,String filename);
    


    void createGroup(String groupName, ArrayList<String> groupMembers);

    ArrayList<Message> getHistory(String receiver);
    
    ArrayList<Pair> getContactsWithType();
    
    void loadErrorServer();
    
    void reciveSponser(byte[] data, int dataLength);
    
     /**
     * send mail to user
     * @param to
     * @param emailBody
     * @return true if success 
     */
    boolean sendMail(String to , String emailBody);
    String getGender(String username);
    User getUser(String userName);
}

