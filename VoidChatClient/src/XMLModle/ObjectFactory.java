//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.17 at 02:36:46 PM EET 
//


package XMLModle;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the XMLModle package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _History_QNAME = new QName("", "history");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: XMLModle
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link HistoryType }
     * 
     */
    public HistoryType createHistoryType() {
        return new HistoryType();
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HistoryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "history")
    public JAXBElement<HistoryType> createHistory(HistoryType value) {
        return new JAXBElement<HistoryType>(_History_QNAME, HistoryType.class, null, value);
    }

}
