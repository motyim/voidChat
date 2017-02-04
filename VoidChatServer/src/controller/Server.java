
package controller;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import javafx.application.Application;
import model.ServerModel;
import view.ServerScene;


public class Server implements ServerControllerInt {
    
    public static void main(String[] args) {
        new Server();
        Application.launch(ServerScene.class, args);
    
    }
    
    public Server(){
		try{
		    ServerModel voidChatServer = new ServerModel();
                    Registry reg = LocateRegistry.getRegistry();
                    reg.rebind("voidChatServer",voidChatServer);
		}catch(RemoteException ex){
			ex.printStackTrace();
		}
	}

    @Override
    public void notify(String SenderName, String reciverName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
}
