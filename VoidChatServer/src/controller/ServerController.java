package controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import model.ClientModelInt;
import model.MailModel;
import model.Message;
import model.ServerModel;
import model.ServerPrivateModel;
import model.User;
import model.UserFx;
import utilitez.Pair;
import view.ServerView;

public class ServerController implements ServerControllerInt {

    private HashMap<String, ClientModelInt> onlineUsers = new HashMap<>();
    private HashMap<String, ArrayList<String>> groups = new HashMap<>();

    private ServerModel model;
    private ServerView view;

    private Registry reg;

    private ServerPrivateModel privateModel;

    private byte[] sponserImage;
    private String serverNotifaction;
    
    private Thread checkOnline ;

    public ServerController(ServerView view) {
        try {

            //conncet to view
            this.view = view;
            //connect to model 
            model = new ServerModel(this);

            //connect to private model
            privateModel = new ServerPrivateModel(this);

            //upload to registry
            reg = LocateRegistry.createRegistry(1050);

            serverNotifaction = "Void Chat Team Yor7b bekom :) ";

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {

        try {
            reg.rebind("voidChatServer", model);
            
            //method to check online users 
            checkOnline = new Thread(()->{
                while(true)
                    checkOnlines();
            });
            checkOnline.start();
            //set all user offline 
            model.setAllUserOffline();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stopServer() {
        try {
            
            reg.unbind("voidChatServer");
            checkOnline.stop();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        } catch (NotBoundException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Application.launch(ServerView.class, args);
    }

    @Override
    public void notify(String reciver, String message, int type) {

        if (onlineUsers.containsKey(reciver)) {

            ClientModelInt clientObject = onlineUsers.get(reciver);
            try {
                clientObject.notify(message, type);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }


    @Override
    public void recieveMsg(Message message) {
        
        String reciever = message.getTo();
        
        if ((reciever.contains("##"))) {
            if (groups.containsKey(reciever)) {
                ArrayList<String> chatMembers = groups.get(reciever);

                for (int i = 0; i < chatMembers.size(); i++) {
                  
                    if (!chatMembers.get(i).equals(message.getFrom())) {
                        if (onlineUsers.containsKey(chatMembers.get(i))) {
                          
                            try {

                                ClientModelInt clientObject = onlineUsers.get(chatMembers.get(i));

                                clientObject.reciveMsg(message);
                            } catch (RemoteException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } else if (onlineUsers.containsKey(reciever)) {
            ClientModelInt clientObject = onlineUsers.get(reciever);
            try {
                
                clientObject.reciveMsg(message);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

   

    @Override
    public boolean register(String username, ClientModelInt obj) {
        if (onlineUsers.containsKey(username)) {
            return false;
        }
        onlineUsers.put(username, obj);
        sendServerNotifcation(obj); //update message and sponcer in recently login user
      
        return true;
    }

    @Override
    public User getUserInfo(String username) {
        return privateModel.getUserInfo(username);
    }

    @Override
    public void sendAnnouncement(String message) {

        //save on local 
        serverNotifaction = message;

        Set<String> onlineSet = onlineUsers.keySet();
        onlineSet.forEach((user) -> {
            try {
                onlineUsers.get(user).receiveAnnouncement(message);
            } catch (RemoteException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    @Override
    public void unregister(String username) {
       
        onlineUsers.remove(username);
       
    }

    @Override
    public ClientModelInt getConnection(String Client) {

        if (onlineUsers.containsKey(Client)) {
            return onlineUsers.get(Client);
        }

        return null;
    }

    @Override
    public void createGroup(String groupName, ArrayList<String> groupMembers) {
        groups.put(groupName, groupMembers);
    }

    @Override
    public void sendSponser(byte[] data, int dataLength) {
        sponserImage = new byte[dataLength];

        //copy bytes in local array
        for (int i = 0; i < dataLength; i++) {
            sponserImage[i] = data[i];
        }

        //get all online users
        Set<String> onlineSet = onlineUsers.keySet();
        onlineSet.forEach((user) -> {
            try {
                onlineUsers.get(user).reciveSponser(sponserImage, dataLength);
            } catch (RemoteException ex) {
                Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void sendServerNotifcation(ClientModelInt obj) {
        Thread tr = new Thread() {
            @Override
            public void run() {

                try {
                    Thread.sleep(5000); //wait untile chatbox load 

                    obj.receiveAnnouncement(serverNotifaction);

                    if (sponserImage != null) {
                        obj.reciveSponser(sponserImage, sponserImage.length);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        };

        tr.start();

    }

    @Override
    public boolean sendMail(String to, String subject, String emailBody) {
        return new MailModel(to, subject, emailBody).sendMail();
    }

    /**
     * send welcome mail for first sign up user
     *
     * @param mail
     * @param username
     * @param password
     */
    public void sendWelcomeMail(String mail, String username, String password) {
        Thread tr = new Thread(() -> {
            String message = "<h1>Welcome to Void Chat</h1> <br/> your username: "
                    + username + "<br/>your password : " + password
                    + "<br/>we are waiting for you .. just login and have fun ;)";
            sendMail(mail, "Welcome To Void Chat", message);
        });
        tr.start();
    }

    public ArrayList<User> getAllUsers() {
        if (model.getAllUsers() != null) {
            return model.getAllUsers();
        }
        return null;
    }

    public void updateUser(User user) {
        model.updateUser(user);
    }

    public void GenerateUserFX(UserFx user) {
        view.GenerateUserFX(user);

    }

    //-------------- Merna ------------------
    public ArrayList<Integer> getStatistics() {
        return model.getStatistics();
    }

    public ArrayList<Pair> getCountries() {
        return model.getCountries();
    }

    public ArrayList<Pair> getGender() {
        return model.getGender();
    }
    //-------------- End Merna ------------------

    //-------------- Roma ------------------
    //-------------- End roma ------------------
    //-------------- Motyim ------------------
    
    //method to check online users and remove not active user
     private void checkOnlines(){
        try {
            
            Thread.sleep(5000);
            Set<String> onlineSet = onlineUsers.keySet();
            onlineSet.forEach((user) -> {
                try {
                    //user is not online
                    onlineUsers.get(user).isOnline();
                } catch (RemoteException ex) {
                    try {
                        ex.printStackTrace();
                        //handle set user offline
                        model.changeStatus(user , "offline");
                        //remove user from hashmap
                        onlineUsers.remove(user);
                        
                    } catch (RemoteException ex1) {
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     }

    //-------------- End motyim ------------------

    @Override
    public void loadErrorServer() {
        for(String key : onlineUsers.keySet()){
             ClientModelInt clientObject=onlineUsers.get(key);
             try {
                    clientObject.loadErrorServer();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

}
