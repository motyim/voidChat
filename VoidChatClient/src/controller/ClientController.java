package controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import model.ClientModel;
import model.ClientModelInt;
import model.ServerModelInt;
import model.User;
import view.ClientView;

public class ClientController implements ClientControllerInt {

    private ClientView view;
    private ClientModel model;
    private ServerModelInt serverModelInt;
    private User loginUser;

    public ClientController(ClientView view) {

        try {

            //connect with view
            this.view = view;

            //connect with model 
            model = new ClientModel(this);

            Registry reg = LocateRegistry.getRegistry("localhost");

            serverModelInt = (ServerModelInt) reg.lookup("voidChatServer");
            System.out.println("Conncet to Server");
            serverModelInt.displayStatus();
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(ClientView.class, args);

    }

    @Override
    public boolean signup(User user) throws Exception {

        try {
            return serverModelInt.signup(user);
        } catch (RemoteException | NullPointerException ex) {
            ex.printStackTrace();
            throw new Exception("Server not working now");
        }

    }

    @Override
    public User signin(String username, String password) throws Exception {

        try {
            //assigne data return to loginUser 
            loginUser =  serverModelInt.signin(username, password);
            //register client to server
            registerToServer(loginUser.getUsername(), model);
        } catch (RemoteException | NullPointerException ex) {
            ex.printStackTrace();
            throw new Exception("Server not working now");
        }
            return loginUser; // return null if faild 
       
    }

    @Override
    public void loadHomePage(User client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerToServer(String username, ClientModelInt obj) {
        try {
            serverModelInt.register(username, obj);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<User> getContacts() {
        try {
            return serverModelInt.getContacts(loginUser.getUsername());
        } catch (RemoteException ex) {
             ex.printStackTrace();
             return null;
        }
        
    }

    @Override
    public ArrayList<String> checkRequest() {
        try {
            return serverModelInt.checkRequest(loginUser.getUsername());
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void changeStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logout() {
        try {
            //System.out.println(userName);
            serverModelInt.unregister(loginUser.getUsername());
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int sendRequest(String reciverName,String category) {
        System.out.println("in client Controller "+ reciverName+" "+category);
        try {
            return serverModelInt.sendRequest(loginUser.getUsername(), reciverName, category);
        } catch (RemoteException ex) {
            System.out.println("Exception in client controller");
            ex.printStackTrace();
            return 0;
        }
        
    }

    @Override
    public void notify(String senderName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void acceptRequest(String senderName, String reciverName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMsg(String reciver, String Msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsg(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void groupMsg(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
