
package edu.txstate.cs4398.vc.desktop.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the edu.txstate.cs4398.vc.desktop.services package. 
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

    private final static QName _GetVideoByName_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getVideoByName");
    private final static QName _EchoResponse_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "echoResponse");
    private final static QName _GetVideoByUPCResponse_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getVideoByUPCResponse");
    private final static QName _Video_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "video");
    private final static QName _Category_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "category");
    private final static QName _AddVideo_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "addVideo");
    private final static QName _GetProductName_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getProductName");
    private final static QName _GetProductNameResponse_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getProductNameResponse");
    private final static QName _Echo_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "echo");
    private final static QName _GetVideoByUPC_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getVideoByUPC");
    private final static QName _AddVideoResponse_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "addVideoResponse");
    private final static QName _Person_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "person");
    private final static QName _GetVideoByNameResponse_QNAME = new QName("http://services.desktop.vc.cs4398.txstate.edu/", "getVideoByNameResponse");
    private final static QName _PersonDirectedVideos_QNAME = new QName("", "directedVideos");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: edu.txstate.cs4398.vc.desktop.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Person }
     * 
     */
    public Person createPerson() {
        return new Person();
    }

    /**
     * Create an instance of {@link GetVideoByNameResponse }
     * 
     */
    public GetVideoByNameResponse createGetVideoByNameResponse() {
        return new GetVideoByNameResponse();
    }

    /**
     * Create an instance of {@link GetVideoByUPC }
     * 
     */
    public GetVideoByUPC createGetVideoByUPC() {
        return new GetVideoByUPC();
    }

    /**
     * Create an instance of {@link Video }
     * 
     */
    public Video createVideo() {
        return new Video();
    }

    /**
     * Create an instance of {@link GetProductNameResponse }
     * 
     */
    public GetProductNameResponse createGetProductNameResponse() {
        return new GetProductNameResponse();
    }

    /**
     * Create an instance of {@link GetProductName }
     * 
     */
    public GetProductName createGetProductName() {
        return new GetProductName();
    }

    /**
     * Create an instance of {@link AddVideoResponse }
     * 
     */
    public AddVideoResponse createAddVideoResponse() {
        return new AddVideoResponse();
    }

    /**
     * Create an instance of {@link Echo }
     * 
     */
    public Echo createEcho() {
        return new Echo();
    }

    /**
     * Create an instance of {@link AddVideo }
     * 
     */
    public AddVideo createAddVideo() {
        return new AddVideo();
    }

    /**
     * Create an instance of {@link Category }
     * 
     */
    public Category createCategory() {
        return new Category();
    }

    /**
     * Create an instance of {@link GetVideoByName }
     * 
     */
    public GetVideoByName createGetVideoByName() {
        return new GetVideoByName();
    }

    /**
     * Create an instance of {@link EchoResponse }
     * 
     */
    public EchoResponse createEchoResponse() {
        return new EchoResponse();
    }

    /**
     * Create an instance of {@link GetVideoByUPCResponse }
     * 
     */
    public GetVideoByUPCResponse createGetVideoByUPCResponse() {
        return new GetVideoByUPCResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVideoByName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getVideoByName")
    public JAXBElement<GetVideoByName> createGetVideoByName(GetVideoByName value) {
        return new JAXBElement<GetVideoByName>(_GetVideoByName_QNAME, GetVideoByName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EchoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "echoResponse")
    public JAXBElement<EchoResponse> createEchoResponse(EchoResponse value) {
        return new JAXBElement<EchoResponse>(_EchoResponse_QNAME, EchoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVideoByUPCResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getVideoByUPCResponse")
    public JAXBElement<GetVideoByUPCResponse> createGetVideoByUPCResponse(GetVideoByUPCResponse value) {
        return new JAXBElement<GetVideoByUPCResponse>(_GetVideoByUPCResponse_QNAME, GetVideoByUPCResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Video }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "video")
    public JAXBElement<Video> createVideo(Video value) {
        return new JAXBElement<Video>(_Video_QNAME, Video.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Category }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "category")
    public JAXBElement<Category> createCategory(Category value) {
        return new JAXBElement<Category>(_Category_QNAME, Category.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddVideo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "addVideo")
    public JAXBElement<AddVideo> createAddVideo(AddVideo value) {
        return new JAXBElement<AddVideo>(_AddVideo_QNAME, AddVideo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getProductName")
    public JAXBElement<GetProductName> createGetProductName(GetProductName value) {
        return new JAXBElement<GetProductName>(_GetProductName_QNAME, GetProductName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetProductNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getProductNameResponse")
    public JAXBElement<GetProductNameResponse> createGetProductNameResponse(GetProductNameResponse value) {
        return new JAXBElement<GetProductNameResponse>(_GetProductNameResponse_QNAME, GetProductNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Echo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "echo")
    public JAXBElement<Echo> createEcho(Echo value) {
        return new JAXBElement<Echo>(_Echo_QNAME, Echo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVideoByUPC }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getVideoByUPC")
    public JAXBElement<GetVideoByUPC> createGetVideoByUPC(GetVideoByUPC value) {
        return new JAXBElement<GetVideoByUPC>(_GetVideoByUPC_QNAME, GetVideoByUPC.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddVideoResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "addVideoResponse")
    public JAXBElement<AddVideoResponse> createAddVideoResponse(AddVideoResponse value) {
        return new JAXBElement<AddVideoResponse>(_AddVideoResponse_QNAME, AddVideoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Person }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "person")
    public JAXBElement<Person> createPerson(Person value) {
        return new JAXBElement<Person>(_Person_QNAME, Person.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetVideoByNameResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://services.desktop.vc.cs4398.txstate.edu/", name = "getVideoByNameResponse")
    public JAXBElement<GetVideoByNameResponse> createGetVideoByNameResponse(GetVideoByNameResponse value) {
        return new JAXBElement<GetVideoByNameResponse>(_GetVideoByNameResponse_QNAME, GetVideoByNameResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "directedVideos", scope = Person.class)
    @XmlIDREF
    public JAXBElement<Object> createPersonDirectedVideos(Object value) {
        return new JAXBElement<Object>(_PersonDirectedVideos_QNAME, Object.class, Person.class, value);
    }

}
