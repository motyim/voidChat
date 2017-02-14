package controller;

import java.io.File;
import java.util.ArrayList;
import model.ClientModelInt;
import model.Message;
import model.User;

public interface ClientControllerInt {

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
     * call loadhomepage in client view
     *
     * @param client
     */
    void loadHomePage(User client);

    /**
     * call register in SM
     *
     * @param username
     * @param obj
     */
    void registerToServer(String username, ClientModelInt obj);

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

    void notifyStatus(String username, String status);

    //void sendMsg(String reciver, String Msg);
    void sendMsg(Message message);

    //void reciveMsg(String msg);
    void reciveMsg(Message message);

    void groupMsg(String msg, ArrayList<String> groupChatUsers);

    void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers);

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
     * @return url location or null if not file choosen
     * @throws RemoteException
     */
    String getSaveLocation(String sender);




    void createGroup(String groupName, ArrayList<String> groupMembers);

    public ArrayList<Message> getHistory(String receiver);

}
