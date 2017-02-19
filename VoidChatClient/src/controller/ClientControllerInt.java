package controller;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import model.ClientModelInt;
import model.Message;
import model.User;
import utilitez.Pair;

public interface ClientControllerInt {
    
    /**
     * connect to server by host ip
     * @param host
     * @return 
     */
    public boolean conncetToServer(String host);

    /**
     * validate & create user obj send to server Model
     *
     * @param user
     * @return
     */
    boolean signup(User user) throws Exception;

    /**
     * validate -> false call show Error in CV -> true create user OBj and ..
     * send to SM if get object make this :D 1- load user home page 2- register
     * to server 3- getContact 4- check friend requests .
     *
     * @param username
     * @param password
     * @return User Object
     * @thows Exception
     */
    User signin(String username, String password) throws Exception;

    

    /**
     * call register in SM
     *
     * @param username
     * @param obj
     * @throws java.lang.Exception
     */
    void registerToServer(String username, ClientModelInt obj) throws Exception;

    /**
     * call get Contacts in SM
     *
     * @return array list of friends
     */
    ArrayList<User> getContacts();

    /**
     * take my username and call checkRequest in SM
     *
     * @return list of friends request
     */
    ArrayList<String> checkRequest();

    /**
     *
     * @param status
     */
    void changeStatus(String status);

    /**
     * call unregister from SM handle out form chat mn error w kda
     */
    void logout();

    //zwadt 2l category
    int sendRequest(String reciverName, String category);

    void notify(String message, int type);

    boolean acceptRequest(String friendName);

    

    //void sendMsg(String reciver, String Msg);
    void sendMsg(Message message);

    //void reciveMsg(String msg);
    void reciveMsg(Message message);

    

    /**
     * get login user info 
     * @return Login User
     */
    User getUserInformation();

    /**
     * get Announcement from Server
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

    public ArrayList<Message> getHistory(String receiver);
    
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
