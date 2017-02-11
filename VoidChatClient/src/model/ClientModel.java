package model;

import controller.ClientController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author MotYim
 */
public class ClientModel extends UnicastRemoteObject implements ClientModelInt {

    private ClientController controller ;

    public ClientModel(ClientController controller) throws RemoteException {
        this.controller = controller;
        System.out.println("Model created");
    }
    
    @Override
    //////////////////////////3adlt hna//////////////////////////////////////////////////
    public void notify(String message,int type) {
        controller.notify(message ,type);
        System.out.println("notify in server model");
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
     public void reciveMsg(Message message) {
        controller.reciveMsg(message);
    }
  /*  public void reciveMsg(String msg) {
        controller.reciveMsg(msg);
    }*/

    @Override
    public void reciveMsgGroup(String msg, ArrayList<String> groupChatUsers) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receiveAnnouncement(String message) {
        controller.receiveAnnouncement(message);
    }
    
    
    
}
