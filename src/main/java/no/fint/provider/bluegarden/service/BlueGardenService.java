package no.fint.provider.bluegarden.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.service.mapper.OrganisasjonselementMapper;
import no.fint.provider.bluegarden.soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BlueGardenService {

    @Value("${bluegarden.employee-endpoint}")
    private String employeeEnpoint;

    @Value("${bluegarden.org-endpoint}")
    private String orgEnpoint;

    @Value("${bluegarden.username}")
    private String username;

    @Value("${bluegarden.password}")
    private String password;

    @Value("${bluegarden.employer}")
    private String employer;

    @Value("${bluegarden.orgunitid}")
    private String orgUnitId;

    @Value("${bluegarden.source-company}")
    private String sourceCompany;

    @Value("${bluegarden.source-system}")
    private String sourceSystem;

    @Value("${bluegarden.source-user}")
    private String sourceUser;

    @Value("${bluegarden.source-employer}")
    private String sourceEmployer;


    @Autowired
    private OrganisasjonselementMapper organisasjonselementMapper;

    private GetAnsattListSOAPQSService employeeService;
    private GetAnsattList employeePort;
    private BasicHttpBindingIOrgStructureServiceQSService orgService;
    private IOrgStructureService orgPort;
    private Map<String, Object> employeeRequestContext;
    private Map<String, Object> orgRequestContext;
    private BSBHeaderType header;
    private GetAnsattListRequestMessageType employeRequest;
    private GetOrgListRequest orgRequest;


    private List<FintResource<Organisasjonselement>> organisasjonselementList;

    @PostConstruct
    public void init() {
        employeeService = new GetAnsattListSOAPQSService();
        orgService = new BasicHttpBindingIOrgStructureServiceQSService();
        employeePort = employeeService.getGetAnsattListSOAPQSPort();
        orgPort = orgService.getBasicHttpBindingIOrgStructureServiceQSPort();
        employeeRequestContext = ((BindingProvider) employeePort).getRequestContext();
        orgRequestContext = ((BindingProvider) orgPort).getRequestContext();

        employeeRequestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, employeeEnpoint);
        employeeRequestContext.put(BindingProvider.USERNAME_PROPERTY, username);
        employeeRequestContext.put(BindingProvider.PASSWORD_PROPERTY, password);

        orgRequestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, orgEnpoint);
        orgRequestContext.put(BindingProvider.USERNAME_PROPERTY, username);
        orgRequestContext.put(BindingProvider.PASSWORD_PROPERTY, password);

        header = new BSBHeaderType();
        header.setSourceCompany(sourceCompany);
        header.setSourceSystem(sourceSystem);
        header.setSourceUser(sourceUser); // innlogget bruker, sporing av klienten som sender employeRequest
        // employer("*")
        header.getSourceEmployer().add(sourceEmployer);
        header.setUserArea(new UserAreaType());

        employeRequest = new GetAnsattListRequestMessageType();
        employeRequest.setArbeidsgiver(employer);

        orgRequest = new GetOrgListRequest();
        orgRequest.setArbeidsgiver(employer);
        orgRequest.setOrgUnitId(orgUnitId);

        try {
            getBlueGardenData();
        } catch (Exception ignore) {
            getBlueGardenData();
        }

    }

    public void getBlueGardenData() {

        Long startTimestamp = System.currentTimeMillis();
        Long endTimestamp;
        List<AnsattObject> employeeList = new ArrayList<>();
        List<OrgListItemObject> orgListItemObjects = getOrganisationStructure();

        orgListItemObjects.forEach(org -> {
            if (org.isErAktiv()) {
                log.info("Getting employees for -- {}", org.getOrgNavn());
                employeeList.addAll(getEmployeesByOrgUnit(org.getOrgUnitId()));
            } else {
                log.info("OrgUnit is not active", org.getOrgNavn());
            }
        });

        endTimestamp = System.currentTimeMillis();


        organisasjonselementList = organisasjonselementMapper.organisasjonselementMapper(orgListItemObjects, employeeList);

        log.info("Getting employees took {} seconds", (endTimestamp - startTimestamp) / 1000);

    }

    public List<FintResource<Organisasjonselement>> getOrganisasjonselementList() {
        return organisasjonselementList;
    }

    private List<AnsattObject> getEmployeesByOrgUnit(String orgUnitId) {
        employeRequest.setOrgUnitId(orgUnitId);
        header.setMessageId(UUID.randomUUID().toString());
        log.info("Sending getAnsattList employeRequest with MessageId: {} to Bluegarden.", header.getMessageId());
        GetAnsattListResultMessageType employees = employeePort.getAnsattList(employeRequest, header);
        return employees.getAnsattList().getAnsatt();

    }

    public List<OrgListItemObject> getOrganisationStructure() {
        header.setMessageId(UUID.randomUUID().toString()); // sporing, opprettes av klienten

        log.info("Sending getOrgList employeRequest with MessageId: {} to Bluegarden.", header.getMessageId());
        GetOrgListResponse orgListResponse = orgPort.getOrgList(orgRequest, header);

        return orgListResponse.getOrgList().get(0).getOrgUnit();
    }


}
