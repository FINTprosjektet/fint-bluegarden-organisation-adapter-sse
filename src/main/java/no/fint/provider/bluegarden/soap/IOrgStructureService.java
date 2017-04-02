package no.fint.provider.bluegarden.soap;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 3.1.9
 * 2017-01-17T12:04:58.028+01:00
 * Generated source version: 3.1.9
 * 
 */
@WebService(targetNamespace = "http://bluegarden.no/organisation/structure/wsdl/v31", name = "IOrgStructureService")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface IOrgStructureService {

    @WebMethod(action = "getOrgUnit")
    @WebResult(name = "getOrgUnitResponse", targetNamespace = "http://bluegarden.no/organisation/structure/service/v31", partName = "getOrgUnit_Response")
    public GetOrgUnitResponse getOrgUnit(
            @WebParam(partName = "getOrgUnitRequest", name = "getOrgUnitRequest", targetNamespace = "http://bluegarden.no/organisation/structure/service/v31")
                    GetOrgUnitRequest getOrgUnitRequest,
            @WebParam(partName = "BSBHeader", name = "BSBHeader", targetNamespace = "http://bluemsg.bluegarden.no/object/v3", header = true)
                    BSBHeaderType bsbHeader
    );

    @WebMethod(action = "getOrgList")
    @WebResult(name = "getOrgListResponse", targetNamespace = "http://bluegarden.no/organisation/structure/service/v31", partName = "getOrgListResponse")
    public GetOrgListResponse getOrgList(
            @WebParam(partName = "getOrgListRequest", name = "getOrgListRequest", targetNamespace = "http://bluegarden.no/organisation/structure/service/v31")
                    GetOrgListRequest getOrgListRequest,
            @WebParam(partName = "BSBHeader", name = "BSBHeader", targetNamespace = "http://bluemsg.bluegarden.no/object/v3", header = true)
                    BSBHeaderType bsbHeader
    );
}
