package no.fint.provider.bluegarden.soap;

import no.fint.Application;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.1.9
 * 2017-01-17T12:04:58.032+01:00
 * Generated source version: 3.1.9
 * 
 */
@WebServiceClient(name = "BasicHttpBinding_IOrgStructureServiceQSService", 
                  /*wsdlLocation = "file:src/main/resources/schema/OrgStructureBA.wsdl",*/
                  targetNamespace = "http://bluegarden.no/organisation/structure/wsdl/v31") 
public class BasicHttpBindingIOrgStructureServiceQSService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://bluegarden.no/organisation/structure/wsdl/v31", "BasicHttpBinding_IOrgStructureServiceQSService");
    public final static QName BasicHttpBindingIOrgStructureServiceQSPort = new QName("http://bluegarden.no/organisation/structure/wsdl/v31", "BasicHttpBinding_IOrgStructureServiceQSPort");
    static final ClassLoader loader = Application.class.getClassLoader();
    static {
        URL url = null;
        try {
            url = new URL(loader.getResource("schema/OrgStructureBA.wsdl").toString());
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(BasicHttpBindingIOrgStructureServiceQSService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:src/main/resources/schema/OrgStructureBA.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public BasicHttpBindingIOrgStructureServiceQSService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BasicHttpBindingIOrgStructureServiceQSService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BasicHttpBindingIOrgStructureServiceQSService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public BasicHttpBindingIOrgStructureServiceQSService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public BasicHttpBindingIOrgStructureServiceQSService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public BasicHttpBindingIOrgStructureServiceQSService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns IOrgStructureService
     */
    @WebEndpoint(name = "BasicHttpBinding_IOrgStructureServiceQSPort")
    public IOrgStructureService getBasicHttpBindingIOrgStructureServiceQSPort() {
        return super.getPort(BasicHttpBindingIOrgStructureServiceQSPort, IOrgStructureService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IOrgStructureService
     */
    @WebEndpoint(name = "BasicHttpBinding_IOrgStructureServiceQSPort")
    public IOrgStructureService getBasicHttpBindingIOrgStructureServiceQSPort(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingIOrgStructureServiceQSPort, IOrgStructureService.class, features);
    }

}
