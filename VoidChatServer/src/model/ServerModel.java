package model;

import controller.ServerController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import utilitez.SHA;
import utilitez.Constant;

/**
 *
 * @author Roma
 */
public class ServerModel extends UnicastRemoteObject implements ServerModelInt {

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String query;
    private String property = System.getProperty("user.dir");
    private ServerController controller;

    public ServerModel(ServerController controller) throws RemoteException {
        this.controller = controller;
    }

    /**
     * connect to database
     */
    private void getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + property + "/voidChat", "", "");
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close connection to database
     */
    private void closeResources() {
        try {
            //resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean signup(User user) throws RemoteException {
        try {
            getConnection();
            query = "select * from UserTable where username = '" + user.getUsername() + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                closeResources();
                return false;
            } else {
                query = "insert into UserTable (username,email,fname,lname,password,gender,country) values('" + user.getUsername()
                        + "','" + user.getEmail() + "','" + user.getFname() + "','" + user.getLname() + "','" + SHA.encrypt(user.getPassword()) + "','"
                        + user.getGender() + "','" + user.getCountry() + "')";
                statement.executeUpdate(query);
                System.out.println("Done");
                closeResources();
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public User signin(String username, String password) throws RemoteException {
        User user = null;
        try {
            getConnection();
            query = "select * from UserTable where username = '" + username + "'and password='" + SHA.encrypt(password) + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String name = resultSet.getString("username");
                String email = resultSet.getString("email");
                String fname = resultSet.getString("fname");
                String lname = resultSet.getString("lname");
                String pw = "";
                String gender = resultSet.getString("gender");
                String status = resultSet.getString("status");
                String country = resultSet.getString("country");
                user = new User(name, email, fname, lname, pw, gender, country, status);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return user;
    }

    @Override
    public void register(String username, ClientModelInt obj) throws RemoteException {
        controller.register(username, obj);
    }

    @Override
    public void unregister(String username) throws RemoteException {
        //System.out.println(username);

    }

    @Override
    public ArrayList<String> checkRequest(String username) throws RemoteException {
        ArrayList<String> friendsNames = null;
        try {
            getConnection();
            query = "select sender from Requests where receiver = '" + username + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                friendsNames = new ArrayList<>();
                friendsNames.add(resultSet.getString("sender"));
                while (resultSet.next()) {
                    friendsNames.add(resultSet.getString("sender"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return friendsNames;
    }

    @Override
    public boolean acceptRequest(String senderName, String reciverName) throws RemoteException {
        try {
            String type = null;
            getConnection();
            query = "select type from Requests where receiver='" + reciverName + "' and sender='" + senderName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                type = resultSet.getString("type");
            }
            query = "insert into Relationship (user,friend,type)values ('" + reciverName + "','" + senderName + "','" + type + "')";
            statement.executeUpdate(query);
            query = "delete from Requests where sender='" + senderName + "' and receiver='" + reciverName + "'";
            statement.executeUpdate(query);
            return true; 
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false ;
        }finally{
            closeResources();
        }
        
    }

    @Override
    //hna zawdt
    public void notify(String senderName, String reciverName) throws RemoteException {
        System.out.println("notify in model controller");
        controller.notify(senderName, reciverName);
    }

    @Override
    public void changeStatus(String username, String status) throws RemoteException {
        try {
            getConnection();
            query = "update UserTable set status='" + status + "' where username= '" + username + "'";
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        closeResources();
    }

    @Override
    public boolean sendMsg(String reciver, String msg) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void groupMsg(String msg, ArrayList<String> groupChatUsers) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<User> getContacts(String userName) throws RemoteException {
        ArrayList<User> friendsObjects = null;
        ArrayList<String> friendsNames = new ArrayList<String>();
        try {
            getConnection();
            query = "select friend from Relationship where user = '" + userName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                friendsObjects = new ArrayList<User>();
                friendsNames.add(resultSet.getString("friend"));
                while (resultSet.next()) {
                    friendsNames.add(resultSet.getString("friend"));
                }
            }

            for (int i = 0; i < friendsNames.size(); i++) {
                query = "select * from UserTable where username = '" + friendsNames.get(i) + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String fname = resultSet.getString("fname");
                    String lname = resultSet.getString("lname");
                    String password = resultSet.getString("password");
                    String gender = resultSet.getString("gender");
                    String status = resultSet.getString("status");
                    String country = resultSet.getString("country");
                    User user = new User(username, email, fname, lname, password, gender, country, status);
                    friendsObjects.add(user);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return friendsObjects;
    }

    @Override
    public int sendRequest(String senderName, String reciverName, String type) throws RemoteException {
        if (senderName.equals(reciverName)) {
            return Constant.SAME_NAME;
        }
        try {
            getConnection();
            query = "select * from UserTable where username='" + reciverName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (!(resultSet.next())) {

                return Constant.USER_NOT_EXIST;
            }

            query = "select * from Relationship where (user='" + senderName + "' and friend='" + reciverName + "') or "
                    + "(user='" + reciverName + "' and friend='" + senderName + "')";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {

                return Constant.ALREADY_FRIENDS;
            }

            query = "select * from Requests where (sender='" + senderName + "' and receiver='" + reciverName + "')or"
                    + "(sender='" + reciverName + "' and receiver='" + senderName + "')";
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {

                return Constant.REQUEST_ALREADY_EXIST;
            }

            query = "insert into Requests (sender,receiver,type)values ('" + senderName + "','" + reciverName + "','" + type + "')";
            statement.executeUpdate(query);

            //zwat hna
            return Constant.SENDED;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Constant.EXCEPTION;
        } finally {
            closeResources();
            System.out.println("$_");
            notify(senderName, reciverName);
            System.out.println("!_");
        }

    }

    @Override
    public void ignoreRequest(String senderName, String reciverName) {
        try {
            getConnection();
            query = "delete from Requests where sender='" + senderName + "' and receiver='" + reciverName + "'";
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
    }
}
