package no.fint.provider.bluegarden.service;

import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.soap.OrgListItemObject;

import java.util.List;

public interface BlueGardenService {
    void getBlueGardenData();

    List<FintResource<Organisasjonselement>> getOrganisasjonselementList();

    List<OrgListItemObject> getOrganisationStructure();
}
