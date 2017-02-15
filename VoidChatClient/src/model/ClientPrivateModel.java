package model;


import XMLModle.FontFamily;
import XMLModle.HistoryType;
import XMLModle.MessageType;
import XMLModle.ObjectFactory;
import controller.ClientController;
import controller.ClientControllerInt;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

            marsh.marshal(createHistory, new FileOutputStream(file));

            //-------------- create XSLT file ------------------
            copyFile(new File("src//XML//history.xsl"), file);
            //-------------- create XSD file ------------------
            copyFile(new File("src//XML//history.xsd"), file);

        } catch (JAXBException | FileNotFoundException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * copy file in path
     * @param file
     * @param path
     */
    private void copyFile(File file, File path) {

        Thread th = new Thread(() -> {

            String newFile = path.getParent() + "\\" + file.getName();
            System.out.println(newFile);

            try (FileReader fr = new FileReader(file);
                    FileWriter fw = new FileWriter(newFile)) {

                int readByte;
                while ((readByte = fr.read()) != -1) {
                    fw.write(readByte);
                }

            } catch (FileNotFoundException ex) {
                Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ClientPrivateModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        );
        
        th.start();

    }

}
