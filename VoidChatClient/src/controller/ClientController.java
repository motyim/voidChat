package controller;

import java.io.File;
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
import model.ClientPrivateModel;
import model.Message;
import model.ServerModelInt;
import model.User;
import utilitez.Notification;
import view.ClientView;

public class ClientController implements ClientControllerInt {

    private ClientView view;
    private ClientModel model;
    private ClientPrivateModel pmodel;
    private ServerModelInt serverModelInt;
    private User loginUser;

    public ClientController(ClientView view) {

        try {

            //connect with view
            this.view = view;

            //connect with model 
            model = new ClientModel(this);

            //connect to private model
            pmodel = new ClientPrivateModel(this);

            Registry reg = LocateRegistry.getRegistry(1050);

            serverModelInt = (ServerModelInt) reg.lookup("voidChatServer");
            System.out.println("Conncet to Server");
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
            loginUser = serverModelInt.signin(username, password);
            //register client to server
            if (loginUser != null) {
                registerToServer(loginUser.getUsername(), model);
                System.out.println(">><<>>" + loginUser.getUsername());
            }
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
            System.out.println("bl");
            serverModelInt.register(username, obj);
            System.out.println("blabla");
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
////////////////////////////////////////////////////////////

    @Override
    public void changeStatus(String status) {
        try {
            serverModelInt.changeStatus(loginUser.getUsername(), status);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
/////////////////////////////////////////////////////////////////////////

    @Override
    public void logout() {
        try {
            //System.out.println(userName);
            serverModelInt.unregister(loginUser.getUsername());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int sendRequest(String reciverName, String category) {
        System.out.println("in client Controller " + reciverName + " " + category);
        try {
            System.out.println("my name is " + loginUser.getUsername());
            return serverModelInt.sendRequest(loginUser.getUsername(), reciverName, category);
        } catch (RemoteException ex) {
            System.out.println("Exception in client controller");
            ex.printStackTrace();
            return 0;
        }

    }

    @Override
    public void notify(String message, int type) {
        view.notify(message, type);
        System.out.println("notify in client controller");
    }

    @Override
    public boolean acceptRequest(String friendName) {
        try {
            return serverModelInt.acceptRequest(friendName, loginUser.getUsername());
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void sendMsg(Message message) {
        try {
            System.out.println("in client controller send msg");
            serverModelInt.sendMsg(message);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    /*    public void sendMsg(String friendName,String message) {
        try {
            System.out.println("send Message in client controller "+friendName+" "+message);
            serverModelInt.sendMsg(friendName, message);
        } catch (RemoteException ex) {
           ex.printStackTrace();
        }
    }*/
    @Override
    public void reciveMsg(Message message) {
        view.reciveMsg(message);
    }

    /*  public void reciveMsg(String msg) {
         view.reciveMsg(msg);
    }*/
    @Override
    public void groupMsg(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public User getUserInformation() {
        System.out.println("here" + this.loginUser);
        return this.loginUser;
    }

    @Override
    public void receiveAnnouncement(String message) {
        view.receiveAnnouncement(message);
        view.notify("New Message from Server Open Home to See it", Notification.SERVER_MESSAGE);
    }

    public void ignoreRequest(String senderName) {
        try {
            serverModelInt.ignoreRequest(senderName, loginUser.getUsername());
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void saveXMLFile(File file, ArrayList<Message> messages) {
        pmodel.saveXMLFile(file, messages, loginUser);
    }

    @Override
    public ClientModelInt getConnection(String Client) {
        try {
            return serverModelInt.getConnection(Client);
        } catch (RemoteException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getSaveLocation(String sender) {
        return view.getSaveLocation(sender);
    }

    @Override
    public User getLoginUser() {
        return loginUser;
    }

    @Override
    public void createGroup(String groupName, ArrayList<String> groupMembers) {
        try {
            serverModelInt.createGroup(groupName, groupMembers);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public ArrayList<Message> getHistory(String receiver) {
        try {
            return  serverModelInt.getHistory(loginUser.getUsername(), receiver);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public void errorServer() {
        view.errorServer();
    }
    
    
}
