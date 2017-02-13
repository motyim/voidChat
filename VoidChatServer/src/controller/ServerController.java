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
import model.Message;
import model.ServerModel;
import model.ServerPrivateModel;
import model.User;
import view.ServerView;

public class ServerController implements ServerControllerInt {

    private HashMap<String, ClientModelInt> onlineUsers = new HashMap<>();
    private HashMap<String, ArrayList<String>> groups = new HashMap<String, ArrayList<String>>();
    
    private ServerModel model;
    private ServerView view;

    private Registry reg;

    private ServerPrivateModel privateModel;

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
                    if(!chatMembers.get(i).equals(message.getFrom())){
                    if (onlineUsers.containsKey(chatMembers.get(i))) {
                        System.out.println(chatMembers.get(i) + " is online and group msg chat will send");
                        try {
                            
                            ClientModelInt clientObject = onlineUsers.get(chatMembers.get(i));
                            
                            clientObject.reciveMsg(message);
                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                        }
                    }}
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

    /* public boolean sendMsg(String reciver, String msg) {
        System.out.println("send message in server controller");
        System.out.println("size of online " + onlineUsers.size());
        if (onlineUsers.containsKey(reciver)) {
            System.out.println("User is online before try");
            ClientModelInt clientObject = onlineUsers.get(reciver);
            try {

                clientObject.reciveMsg(msg);
                System.out.println("User is online");
                return true;
            } catch (RemoteException ex) {
                ex.printStackTrace();
                System.out.println("Exception Happen");
            }
        }
        return false;

    }*/
    @Override
    public void groupMsg(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void register(String username, ClientModelInt obj) {
        onlineUsers.put(username, obj);
        System.out.println("-- user login --" + onlineUsers.size());
    }

    @Override
    public User getUserInfo(String username) {
        return privateModel.getUserInfo(username);
    }

    @Override
    public void sendAnnouncement(String message) {
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
}
