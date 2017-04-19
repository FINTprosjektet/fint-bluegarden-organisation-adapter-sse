package no.fint.provider.bluegarden.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.service.BlueGardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrganisationController {

    @Autowired
    private BlueGardenService blueGardenService;

    @RequestMapping(value = "/organisation", method = RequestMethod.GET)
    public List<FintResource<Organisasjonselement>> getAllOrganisasjonselement() {
        return blueGardenService.getOrganisasjonselementList();
    }

    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public void refresh() {
        blueGardenService.getBlueGardenData();
    }

}
