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
import view.ServerView;

public class ServerController implements ServerControllerInt {

    private HashMap<String, ClientModelInt> onlineUsers = new HashMap<>();
    private HashMap<String, ArrayList<String>> groups = new HashMap<String, ArrayList<String>>();

    private ServerModel model;
    private ServerView view;

    private Registry reg;

    private ServerPrivateModel privateModel;

    private byte[] sponserImage;
    private String serverNotifaction;

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
            
            //method to check online users 
            Thread tr = new Thread(()->{
                while(true)
                    checkOnlines();
            });
            tr.start();
            
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void startServer() {
        System.out.println("Server controller");
        try {
            reg.rebind("voidChatServer", model);

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void stopServer() {
        try {
            System.out.println("Server controller stop server");
            reg.unbind("voidChatServer");
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
        System.out.println("in notify in server controller");
        if (onlineUsers.containsKey(reciver)) {
            System.out.println("online user is " + reciver);
            ClientModelInt clientObject = onlineUsers.get(reciver);
            try {
                clientObject.notify(message, type);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void notifyStatus(String username, String status, ArrayList<String> friends) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recieveMsg(Message message) {
        System.out.println("receieve msg ");
        String reciever = message.getTo();
        System.out.println(reciever);
        if ((reciever.contains("##"))) {
            if (groups.containsKey(reciever)) {
                ArrayList<String> chatMembers = groups.get(reciever);

                for (int i = 0; i < chatMembers.size(); i++) {
                    System.out.println(chatMembers.get(i));
                    if (!chatMembers.get(i).equals(message.getFrom())) {
                        if (onlineUsers.containsKey(chatMembers.get(i))) {
                            System.out.println(chatMembers.get(i) + " is online and group msg chat will send");
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
                System.out.println(reciever + " is online and meg is send");
                clientObject.reciveMsg(message);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void groupMsg(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean register(String username, ClientModelInt obj) {
        if(onlineUsers.containsKey(username))
            return false ;
        onlineUsers.put(username, obj);
        sendServerNotifcation(obj); //update message and sponcer in recently login user
        System.out.println("-- user login --" + onlineUsers.size());
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
        System.out.println(onlineUsers.size());
        onlineUsers.remove(username);
        System.out.println(onlineUsers.size());
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
                    
                    if(sponserImage!=null)
                        obj.reciveSponser(sponserImage, sponserImage.length);
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
    public boolean sendMail(String to , String subject , String emailBody){
        return new MailModel(to,subject, emailBody).sendMail();
    }
    
    /**
     * send welcome mail for first sign up user
     * @param mail
     * @param username
     * @param password 
     */
    public void sendWelcomeMail(String mail , String username , String password){
        Thread tr = new Thread(()->{
            String message = "<h1>Welcome to Void Chat</h1> <br/> your username: "
                        +username+"<br/>your password : "+password
                        + "<br/>we are waiting for you .. just login and have fun ;)";
            sendMail(mail, "Welcome To Void Chat", message);
        });
        tr.start();
    }
    
    public ArrayList<User> getAllUsers(){
         if(model.getAllUsers() != null)
            return model.getAllUsers();
         return null;
     }
    public void updateUser(User user){
        model.updateUser(user);
    }
    public void GenerateUserFX(UserFx user){
        view.GenerateUserFX(user);

    }
    
    
    //-------------- Merna ------------------
    
    //-------------- End Merna ------------------
    
    //-------------- Roma ------------------
    
    //-------------- End roma ------------------
    
    //-------------- Motyim ------------------
    
    //method to check online users and remove not active user
     private void checkOnlines(){
        try {
            System.out.println("Start Check------------------");
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
                        System.err.print("user " + user);
                    } catch (RemoteException ex1) {
                        Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                    System.err.print("user ---> " + user);
                }
            });
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("End Check------------------");
     }
    //-------------- End motyim ------------------
}
