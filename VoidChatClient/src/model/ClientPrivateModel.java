package model;


import XMLModle.FontFamily;
import XMLModle.HistoryType;
import XMLModle.MessageType;
import XMLModle.ObjectFactory;
import controller.ClientControllerInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * ClassName : ClientPrivateModel.java 
 * Description : file for non remote methods
 * @author MotYim
 * @since 12-02-2017
 */
public class ClientPrivateModel {

    ClientControllerInt controller;

    public ClientPrivateModel(ClientControllerInt controller) {
        this.controller = controller;
    }

    public void saveXMLFile(File file, ArrayList<Message> messages, User user) {
        try {

            JAXBContext context = JAXBContext.newInstance("XMLModle");

            ObjectFactory factory = new ObjectFactory();

            HistoryType history = factory.createHistoryType();
            history.setOwner(user.getUsername());

            List<MessageType> messagesXML = history.getMessage();
            
            //-------------- parse object message ot messageType ------------------
            for (Message message : messages) {
                MessageType messageType = factory.createMessageType();
                messageType.setBody(message.getBody());
                messageType.setColor(message.getFontColor());
                messageType.setDate(message.getDate());
                messageType.setFamily(FontFamily.fromValue(message.getFontFamily()));
                messageType.setFrom(message.getFrom());
                messageType.setStyle(message.getFontStyle());
                messageType.setTo(message.getTo());
                messageType.setSize(message.getFontsSize());
                messageType.setWeight(message.getFontWeight());
                String decoration = (message.getUnderline())?"underline":"none";
                messageType.setDecoration(decoration);

                messagesXML.add(messageType);
            }

            JAXBElement<HistoryType> createHistory = factory.createHistory(history);
            Marshaller marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //-------------- set Schcema ------------------
            marsh.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "history.xsd");
            //-------------- set XLST ------------------
            marsh.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                    "<?xml-stylesheet type='text/xsl' href='history.xsl'?>");
            
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            marsh.marshal(createHistory,fileOutputStream );
            fileOutputStream.close();
            

//            //-------------- create XSLT file ------------------
//            copyFile(new File("src//XML//history.xsl"), file);
//            //-------------- create XSD file ------------------
//            copyFile(new File("src//XML//history.xsd"), file);

            copyFile(getClass().getResource("/XML/history.xsl").openStream(), file.getParent() +"/history.xsl");
            copyFile(getClass().getResource("/XML/history.xsd").openStream(), file.getParent() +"/history.xsd");
            

        } catch (JAXBException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * copy file in path
     * @param file
     * @param path
     */
    private void copyFile(InputStream is, String path) {

        Thread th = new Thread(() -> {

            FileOutputStream os = null;
            try {
                File newFile = new File(path);
                os = new FileOutputStream(newFile);
                int readByte ; 
                while((readByte=is.read())!= -1){
                    os.write(readByte);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
             

              

            
            
        }
        );
        
        th.start();

    }

}
