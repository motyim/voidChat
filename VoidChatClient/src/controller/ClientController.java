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
import utilitez.Pair;
import view.ClientView;

public class ClientController implements ClientControllerInt {

    private ClientView view;
    private ClientModel model;
    private ClientPrivateModel pmodel;
    private ServerModelInt serverModelInt;
    private User loginUser;
    private Thread checkServerStatus ;

    public ClientController(ClientView view) {

        try {

            //connect with view
            this.view = view;

            //connect with model 
            model = new ClientModel(this);

            //connect to private model
            pmodel = new ClientPrivateModel(this);

        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch(ClientView.class, args);

    }

    @Override
    public boolean conncetToServer(String host) {
        try {
            Registry reg = LocateRegistry.getRegistry(host, 1050);

            serverModelInt = (ServerModelInt) reg.lookup("voidChatServer");
            return true;
        } catch (RemoteException | NotBoundException ex) {
            ex.printStackTrace();
            return false;
        }

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
            }
            //check server status 
            checkServerStatus = new Thread(()->{
                while(true)
                checkServerStatus();
            });
            checkServerStatus.start();
            
        } catch (RemoteException | NullPointerException ex) {
            ex.printStackTrace();
            throw new Exception("Server not working now");
        }
        return loginUser; // return null if faild 

    }

  

    @Override
    public void registerToServer(String username, ClientModelInt obj) throws Exception {
        try {
            if (!serverModelInt.register(username, obj)) {
                throw new RuntimeException("User already Login");
            }
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
            serverModelInt.unregister(loginUser.getUsername());
            checkServerStatus.stop();
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int sendRequest(String reciverName, String category) {
        try {
            return serverModelInt.sendRequest(loginUser.getUsername(), reciverName, category);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return 0;
        }

    }

    @Override
    public void notify(String message, int type) {
        view.notify(message, type);
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
    public void sendMsg(Message message) {
        try {
            serverModelInt.sendMsg(message);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void reciveMsg(Message message) {
        view.reciveMsg(message);
    }

    @Override
    public User getUserInformation() {
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
    public String getSaveLocation(String sender,String filename) {
        return view.getSaveLocation(sender,filename);
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
            return serverModelInt.getHistory(loginUser.getUsername(), receiver);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Pair> getContactsWithType() {
        try {
            return serverModelInt.getContactsWithType(loginUser.getUsername());
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void loadErrorServer() {
        view.loadErrorServer();
    }

    @Override
    public void reciveSponser(byte[] data, int dataLength) {
        view.reciveSponser(data, dataLength);
    }

    @Override
    public boolean sendMail(String to, String emailBody) {
        try {
            return serverModelInt.sendMail(to, " Mail From " + loginUser.getUsername(), emailBody);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    private void checkServerStatus(){
        
        try {
            Thread.sleep(5000);
            serverModelInt.isOnline();
        } catch (InterruptedException | RemoteException ex) {
            ex.printStackTrace();
            loadErrorServer();
            checkServerStatus.stop();
        }
     }

    @Override
    public String getGender(String username) {
        try {
            return serverModelInt.getGender(username);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUser(String userName) {
        try {
            
            return  serverModelInt.getUser(userName);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    

}
