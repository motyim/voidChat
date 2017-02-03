package controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javafx.application.Application;
import model.ClientModel;
import model.ClientModelInt;
import model.ServerModelInt;
import model.User;
import view.ClientView;


public class ClientController implements ClientControllerInt{
    
    private ClientView view ; 
    private ClientModel model ;
     
    public ClientController(ClientView view){
        try {
            
            //connect with view
            this.view = view ;
            
            //connect with model 
            model = new ClientModel(this);
            
            Registry reg = LocateRegistry.getRegistry("localhost");
            
            ServerModelInt serverModelInt = (ServerModelInt) reg.lookup("voidChatServer");
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
    public boolean signup() {
        System.out.println("Controller");
        return true ; 
    }

    @Override
    public User signin() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void loadHomePage(User client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerToServer(String username, ClientModelInt obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void getContacts() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void checkRequest() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void changeStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void logout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendRequest(String reciverName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
