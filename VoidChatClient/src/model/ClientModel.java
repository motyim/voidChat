package model;

import controller.ClientController;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 *
 * @author MotYim
 */
public class ClientModel extends UnicastRemoteObject implements ClientModelInt, Serializable {

    private ClientController controller;

    public ClientModel(ClientController controller) throws RemoteException {
        this.controller = controller;
        System.out.println("Model created");
    }

    @Override
    //////////////////////////3adlt hna//////////////////////////////////////////////////
    public void notify(String message, int type) {
        controller.notify(message, type);
        System.out.println("notify in server model");
    }

    @Override
    public void notifyStatus(String username, String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reciveMsg(Message message) {
        System.out.println("receive msg in client model" + message.getBody());
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

    @Override
    public String getSaveLocation(String sender) {
        return controller.getSaveLocation(sender);
    }

    @Override
    public void reciveFile(String path, String extension, byte[] data, int dataLength) {
        System.out.println("reciving");

        try {
            File f = new File(path + extension);
            System.out.println("PAATH : " + path + extension);
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(data, 0, dataLength);
            out.flush();
            out.close();
            System.out.println("Done writing data...");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void reciveSponser(byte[] data, int dataLength) throws RemoteException {
        controller.reciveSponser(data, dataLength);
    }

    @Override
    public void loadErrorServer() throws RemoteException {
        controller.loadErrorServer();
    }
}
