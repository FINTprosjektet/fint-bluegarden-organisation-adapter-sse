package no.fint.provider.bluegarden.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.soap.OrgListItemObject;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Profile("test")
@Service
public class MockBlueGardenService implements BlueGardenService {

    @PostConstruct
    public void init() {
        log.info("Started with MockBlueGardenService");
    }


    @Override
    public void getBlueGardenData() {
    }

    @Override
    public List<FintResource<Organisasjonselement>> getOrganisasjonselementList() {
        return null;
    }

    @Override
    public List<OrgListItemObject> getOrganisationStructure() {
        return null;
    }
}
