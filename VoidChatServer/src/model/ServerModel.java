package model;

import controller.ServerController;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import utilitez.SHA;
import utilitez.Constant;
import utilitez.Notification;
import utilitez.Pair;

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
    private boolean isClosed;      //check if databased is closed  

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
            isClosed = false;
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close connection to database
     */
    private void closeResources() {
        try {
            if (!isClosed) {
                //resultSet.close();
                statement.close();
                connection.close();
                isClosed = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public synchronized boolean signup(User user) throws RemoteException {
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
                //add in table
                GenerateUserFX(user);


                closeResources();
                controller.sendWelcomeMail(user.getEmail(), user.getUsername(), user.getPassword());
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized User signin(String username, String password) throws RemoteException {
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
        } finally {
            closeResources();
            return user;
        }

    }

    @Override
    public synchronized boolean register(String username, ClientModelInt obj) throws RemoteException {
        return controller.register(username, obj);
    }

    @Override
    public synchronized void unregister(String username) throws RemoteException {
        controller.unregister(username);
    }

    @Override
    public synchronized ArrayList<String> checkRequest(String username) throws RemoteException {
        ArrayList<String> friendsNames = null;
        try {
            getConnection();
            query = "select sender from Requests where receiver = '" + username + "' And type <> 'Block'";
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
    public synchronized boolean acceptRequest(String senderName, String reciverName) throws RemoteException {
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

            //notify that friend accept friendship
            notify(senderName, reciverName + " Accept Your Friend Request", ACCEPT_FRIEND_REQUEST);

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            closeResources();
        }

    }

    @Override
    //hna zawdt
    public synchronized void notify(String reciver, String message, int type) throws RemoteException {
        controller.notify(reciver, message, type);
    }

    @Override
    public synchronized void changeStatus(String username, String status) throws RemoteException {
        try {
            getConnection();
            query = "update UserTable set status='" + status + "' where username= '" + username + "'";
            statement = connection.createStatement();
            statement.executeUpdate(query);

            ArrayList<User> userFriends = userFriends = getContacts(username);
            if (userFriends != null) {
                for (int i = 0; i < userFriends.size(); i++) {
                    if (status.equalsIgnoreCase("online")) {
                        notify(userFriends.get(i).getUsername(), username + " Become online ", Notification.FRIEND_ONLINE);
                    } else if (status.equalsIgnoreCase("offline")) {
                        notify(userFriends.get(i).getUsername(), username + " Become offline ", Notification.FRIEND_OFFLINE);
                    } else if (status.equalsIgnoreCase("busy")) {
                        notify(userFriends.get(i).getUsername(), username + " Become busy ", Notification.FRIEND_BUSY);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
    }

    @Override
    public synchronized void sendMsg(Message message) {
        if (!message.getTo().contains("##")) {
            insertMessage(message);
        }
        controller.recieveMsg(message);
    }

    @Override
    public synchronized ArrayList<User> getContacts(String userName) throws RemoteException {
        ArrayList<User> friendsObjects = new ArrayList<User>();
        ArrayList<String> friendsNames = new ArrayList<String>();
        try {
            getConnection();
            query = "select * from Relationship where user = '" + userName + "' or friend = '" + userName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                //handle if friend name equals userName
                if (!resultSet.getString("friend").equals(userName)) {
                    friendsNames.add(resultSet.getString("friend"));
                } else {
                    friendsNames.add(resultSet.getString("user"));
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
                    String password = "";
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
        return friendsObjects.size() == 0 ? null : friendsObjects;
    }

    @Override
    public synchronized int sendRequest(String senderName, String reciverName, String type) throws RemoteException {
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
            if(!type.equals("Block"))
                notify(reciverName, senderName + " Want to be your Friend", Notification.FRIEND_REQUSET); //notify if sccuess only

            return Constant.SENDED;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return Constant.EXCEPTION;
        } finally {
            closeResources();
        }

    }

    @Override
    public synchronized void ignoreRequest(String senderName, String reciverName) {
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

    public synchronized ArrayList<Integer> getStatistics() {
        int countUsers = 0;
        ArrayList<Integer> users = new ArrayList<Integer>();
        try {
            getConnection();
            query = "select * from UserTable where status='online'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                countUsers++;
            }
            users.add(countUsers);
            countUsers = 0;

            query = "select * from UserTable where status='offline'";
            ResultSet resultSet2 = statement.executeQuery(query);
            while (resultSet2.next()) {
                countUsers++;

            }
            users.add(countUsers);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return users;
    }

    public synchronized ArrayList<Pair> getGender() {
        int count = 0;
        ArrayList<Pair> users = new ArrayList<Pair>();
        Pair user = new Pair();
        try {
            getConnection();
            query = "select * from UserTable where gender='Female'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                count++;
            }
            user = new Pair("Female", count);
            users.add(user);
            count = 0;
            user = new Pair();
            query = "select * from UserTable where gender='Male'";
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                count++;
            }
            user = new Pair("Male", count);
            users.add(user);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return users;
    }

    public synchronized ArrayList<Pair> getCountries() {
        int count = 0;
        String country = null;
        ArrayList<String> distinctCountries = new ArrayList<String>();
        ArrayList<Pair> countriesPairs = new ArrayList<Pair>();
        Pair myPair = new Pair();

        try {
            getConnection();
            query = "select distinct country from UserTable";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                distinctCountries.add(resultSet.getString("country"));
            }
            for (int i = 0; i < distinctCountries.size(); i++) {
                query = "select * from UserTable where country='" + distinctCountries.get(i) + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    count++;
                    country = resultSet.getString("country");
                }
                myPair = new Pair(country, count);
                countriesPairs.add(myPair);
                count = 0;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return countriesPairs;

    }

    @Override
    public synchronized ClientModelInt getConnection(String Client) {
        return controller.getConnection(Client);
    }

    @Override
    public synchronized void createGroup(String groupName, ArrayList<String> groupMembers) {
        controller.createGroup(groupName, groupMembers);
    }

    public synchronized void insertMessage(Message message) {
        try {
            getConnection();
            query = "insert into Message(fontSize,`from`,`to`,date,fontColor,fontFamily,fontStyle,body,fontWeight,underLine)values (" + message.getFontsSize() + ",'" + message.getFrom() + "','"
                    + message.getTo() + "','" + message.getDate() + "','" + message.getFontColor() + "','" + message.getFontFamily() + "','"
                    + message.getFontStyle() + "','" + message.getBody() + "','" + message.getFontWeight() + "','" + message.getUnderline().toString() + "')";

            statement = connection.createStatement();
            statement.executeUpdate(query);
            closeResources();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public synchronized ArrayList<Message> getHistory(String sender, String receiver) {
        ArrayList<Message> messages = new ArrayList<Message>();
        try {
            getConnection();
            query = "select * from Message where (`from` = '" + sender + "' and `to`='" + receiver + "') or"
                    + " (`to`='" + sender + "'and `from`='" + receiver + "')";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int fontSize = resultSet.getInt(2);
                String from = resultSet.getString(3);
                String to = resultSet.getString(4);
                String date = resultSet.getString(5);
                String fontColor = resultSet.getString(6);
                String fontFamily = resultSet.getString(7);
                String fontStyle = resultSet.getString(8);
                String body = resultSet.getString(9);
                String fontWeight = resultSet.getString(10);
                boolean underline = Boolean.parseBoolean(resultSet.getString(11));

                String format = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
                GregorianCalendar cal = new GregorianCalendar();
                try {
                    cal.setTime(new SimpleDateFormat(format).parse(date));
                    XMLGregorianCalendar calendar;
                    try {
                        calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                        Message message = new Message(fontSize, from, to, calendar, fontColor, fontFamily, fontStyle, body, fontWeight, underline);
                        messages.add(message);
                    } catch (DatatypeConfigurationException ex) {
                        ex.printStackTrace();
                    }

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return messages.size() == 0 ? null : messages;
    }

    @Override
    public synchronized ArrayList<Pair> getContactsWithType(String userName) throws RemoteException {
        ArrayList<String> friendsNames = new ArrayList<String>();
        ArrayList<String> type = new ArrayList<String>();
        ArrayList<Pair> friendPair = new ArrayList<Pair>();

        try {
            getConnection();
            query = "select * from Relationship where user = '" + userName + "' or friend = '" + userName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if (!resultSet.getString("friend").equals(userName)) {
                    friendsNames.add(resultSet.getString("friend"));
                    type.add(resultSet.getString("type"));
                } else {
                    friendsNames.add(resultSet.getString("user"));
                    type.add(resultSet.getString("type"));
                }
            }

            for (int i = 0; i < friendsNames.size(); i++) {
                query = "select * from UserTable where username = '" + friendsNames.get(i) + "'";
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    Pair mypair = new Pair();
                    String username = resultSet.getString("username");
                    String email = resultSet.getString("email");
                    String fname = resultSet.getString("fname");
                    String lname = resultSet.getString("lname");
                    String password = "";
                    String gender = resultSet.getString("gender");
                    String status = resultSet.getString("status");
                    String country = resultSet.getString("country");
                    User user = new User(username, email, fname, lname, password, gender, country, status);
                    mypair.setFirst(user);
                    mypair.setSecond(type.get(i));
                    friendPair.add(mypair);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return friendPair.size() == 0 ? null : friendPair;
    }

    @Override
    public synchronized boolean sendMail(String to, String subject, String emailBody) throws RemoteException {
        return controller.sendMail(to, subject, emailBody);
    }

    public synchronized ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            getConnection();
            query = "select * from UserTable";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String fname = resultSet.getString("fname");
                String lname = resultSet.getString("lname");
                String password = "";
                String gender = resultSet.getString("gender");
                String status = resultSet.getString("status");
                String country = resultSet.getString("country");
                User user = new User(username, email, fname, lname, password, gender, country, status);
                users.add(user);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return users.size() == 0 ? null : users;
    }

    public synchronized void updateUser(User user) {
        try {
            getConnection();
            String query = "update UserTable set fname='" + user.getFname()
                    + "',lname='" + user.getLname() + "',gender='" + user.getGender() + "',country='" + user.getCountry() + "' where username= '" + user.getUsername() + "'";
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
    }

    public synchronized void GenerateUserFX(User user) {
        UserFx userFx = new UserFx(user.getUsername(), user.getEmail(), user.getFname(), user.getLname(), user.getGender(), user.getCountry());

        controller.GenerateUserFX(userFx);
    }

    //-------------- Merna ------------------
    //-------------- End Merna ------------------
    //-------------- Roma ------------------
    //-------------- End roma ------------------
    //-------------- Motyim ------------------
    @Override
    public synchronized boolean isOnline() throws RemoteException {
        return true;
    }

    public synchronized void setAllUserOffline() {
        try {
            getConnection();
            query = "update UserTable set status='offline'";
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
    }

    @Override
    public synchronized String getGender(String username) {
        String gender = null;
        try {
            getConnection();
            query = "select * from UserTable where username='" + username + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                gender = resultSet.getString("gender");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return gender;
    }

    public synchronized User getUser(String userName) {
        User user = null;
        try {
            getConnection();
            query = "select * from UserTable where username='" + userName + "'";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String fname = resultSet.getString("fname");
                String lname = resultSet.getString("lname");
                String password = "";
                String gender = resultSet.getString("gender");
                String status = resultSet.getString("status");
                String country = resultSet.getString("country");
                user = new User(username, email, fname, lname, password, gender, country, status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        closeResources();
        return user;
    }
    //-------------- End Merna ------------------

    //-------------- Roma ------------------
    //-------------- End roma ------------------
    //-------------- Motyim ------------------
    //-------------- End motyim ------------------
}
