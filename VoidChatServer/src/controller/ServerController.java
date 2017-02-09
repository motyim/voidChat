package controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import model.ClientModelInt;
import model.ServerModel;
import model.ServerPrivateModel;
import view.ServerView;

public class ServerController implements ServerControllerInt {

    private HashMap<String, ClientModelInt> onlineUsers = new HashMap<>();

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
           reg = LocateRegistry.getRegistry();
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
    /////////////////////////////////3adlt hna/////////////////////////////////
    public void notify(String SenderName, String reciverName) {
        System.out.println(onlineUsers.size());

        if (onlineUsers.containsKey(reciverName)) {
            System.out.println("server controller");
            ClientModelInt clientObject = onlineUsers.get(reciverName);
            try {
                System.out.println("before");
                clientObject.notify(SenderName);
                System.out.println("after");
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

}
