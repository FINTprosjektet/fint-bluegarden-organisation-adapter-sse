package no.fint.provider.bluegarden.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.service.mapper.OrganisasjonselementMapper;
import no.fint.provider.bluegarden.soap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    @Autowired
    private BlueGardenProps blueGardenProps;

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

        employeeRequestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, blueGardenProps.getEmployeeEnpoint());
        employeeRequestContext.put(BindingProvider.USERNAME_PROPERTY, blueGardenProps.getUsername());
        employeeRequestContext.put(BindingProvider.PASSWORD_PROPERTY, blueGardenProps.getPassword());

        orgRequestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, blueGardenProps.getOrgEnpoint());
        orgRequestContext.put(BindingProvider.USERNAME_PROPERTY, blueGardenProps.getUsername());
        orgRequestContext.put(BindingProvider.PASSWORD_PROPERTY, blueGardenProps.getPassword());

        header = new BSBHeaderType();
        header.setSourceCompany(blueGardenProps.getSourceCompany());
        header.setSourceSystem(blueGardenProps.getSourceSystem());
        header.setSourceUser(blueGardenProps.getSourceUser()); // innlogget bruker, sporing av klienten som sender employeRequest
        // employer("*")
        header.getSourceEmployer().add(blueGardenProps.getSourceEmployer());
        header.setUserArea(new UserAreaType());

        employeRequest = new GetAnsattListRequestMessageType();
        employeRequest.setArbeidsgiver(blueGardenProps.getEmployer());

        orgRequest = new GetOrgListRequest();
        orgRequest.setArbeidsgiver(blueGardenProps.getEmployer());
        orgRequest.setOrgUnitId(blueGardenProps.getOrgUnitId());

        try {
            getBlueGardenData();
        } catch (Exception ignore) {
            getBlueGardenData();
        }
    }

    @Scheduled(initialDelay = 600000L, fixedRate = 600000L)
    public void getBlueGardenDataScheduled() {
        if (blueGardenProps.isSchedulingEnabled()) {
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
        organisasjonselementList = organisasjonselementMapper.convertToResource(orgListItemObjects, employeeList);

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
