package no.fint.provider.bluegarden.controller;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.bluegarden.service.BlueGardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
public class OrganisationController {

    @Autowired
    private BlueGardenService blueGardenService;

    @GetMapping("/organisation")
    public List<FintResource<Organisasjonselement>> getOrganisasjonselementList(@RequestParam(required = false) String organisasjonsId) {
        List<FintResource<Organisasjonselement>> organisasjonselementList = blueGardenService.getOrganisasjonselementList();
        if (StringUtils.isEmpty(organisasjonsId)) {
            return organisasjonselementList;
        } else {
            Optional<FintResource<Organisasjonselement>> organisasjonselement = organisasjonselementList.stream().filter(resource -> resource.getResource().getOrganisasjonsId().getIdentifikatorverdi().equals(organisasjonsId)).findFirst();
            return organisasjonselement.<List<FintResource<Organisasjonselement>>>map(Lists::newArrayList).orElseGet(Collections::emptyList);
        }
    }

    @GetMapping("/refresh")
    public void refresh() {
        blueGardenService.getBlueGardenData();
    }
}
