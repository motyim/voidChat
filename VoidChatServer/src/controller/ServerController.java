package controller;

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
import model.ServerModel;
import model.ServerPrivateModel;
import model.User;
import view.ServerView;

public class ServerController implements ServerControllerInt {

    private HashMap<String, ClientModelInt> onlineUsers = new HashMap<>();

    private ServerModel model;
    private ServerView view;
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
            Registry reg = LocateRegistry.createRegistry(1050);
            reg.rebind("voidChatServer", model);

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(ServerView.class, args);
    }

    @Override
    /////////////////////////////////3adlt hna/////////////////////////////////
    public void notify(String reciver , String message , int type) {

        if (onlineUsers.containsKey(reciver)) {
            ClientModelInt clientObject = onlineUsers.get(reciver);
            try {
                clientObject.notify(message,type);
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
    public boolean sendMsg(String reciver, String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

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

}
