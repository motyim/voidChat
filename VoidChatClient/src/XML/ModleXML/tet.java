/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XML.ModleXML;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author MY-PC
 */
public class tet {
    public static void main(String[] args) {
        
        try {
            JAXBContext context = JAXBContext.newInstance("XML.ModleXML");
            ObjectFactory factory  = new ObjectFactory();
            MessageType message = factory.createMessageType();
            message.setBody("hi from JAXB");
            message.setColor("red");
            message.setDate(null);
            message.setFamily(FontFamily.THREE);
            message.setFrom("ALLZ");
            message.setStyle("asfasf");
            message.setTo("ahmed");
            message.setSize(18);
            
            HistoryType history = factory.createHistoryType();
            history.setOwner("motyim");
            List<MessageType> message1 = history.getMessage();
            message1.add(message);
            JAXBElement<HistoryType> createHistory = factory.createHistory(history);
            Marshaller  marsh = context.createMarshaller();
            marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marsh.marshal(createHistory, new FileOutputStream("fileTest.xml"));
                    
                    } catch (JAXBException ex) {
            Logger.getLogger(tet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(tet.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
}
