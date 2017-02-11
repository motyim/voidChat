/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitez;

/**
 *
 * @author Romisaa
 */
public interface Notification {
    public final static int ACCEPT_FRIEND_REQUEST=0;
    public final static int FRIEND_ONLINE=1;
    public final static int FRIEND_OFFLINE=2;
    public final static int FRIEND_REQUSET=3;
    public final static int SERVER_MESSAGE=4;
    public final static int FRIEND_BUSY=5;
    
    /** 
     * send notification
     * @param reciver
     * @param message
     * @param type
     */
    void notify(String reciver,String message,int type) throws Exception;
}
