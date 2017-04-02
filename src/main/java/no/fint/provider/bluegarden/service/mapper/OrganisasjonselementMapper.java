package no.fint.provider.bluegarden.service.mapper;

import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.felles.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.provider.bluegarden.soap.AnsattObject;
import no.fint.provider.bluegarden.soap.OrgListItemObject;
import no.fint.provider.bluegarden.utilities.ArbeidsforholSystemIdUtility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganisasjonselementMapper {

    public List<FintResource<Organisasjonselement>> organisasjonselementMapper(List<OrgListItemObject> orgListItemObjects, List<AnsattObject> employeeList) {
        List<FintResource<Organisasjonselement>> organisasjonselementList = new ArrayList<>();

        orgListItemObjects.forEach(org -> {
            Organisasjonselement organisasjonselement = new Organisasjonselement();

            organisasjonselement.setNavn(org.getOrgNavn());
            organisasjonselement.setAktiv(org.isErAktiv());

            Identifikator organisasjonsId = new Identifikator();
            organisasjonsId.setIdentifikatorverdi(org.getGroupId());
            organisasjonselement.setOrganisasjonsId(organisasjonsId);


            Identifikator organisasjonsKode = new Identifikator();
            organisasjonsKode.setIdentifikatorverdi(org.getOrgUnitId());
            organisasjonselement.setOrganisasjonsKode(organisasjonsKode);

            List<Relation> relations = new ArrayList<>();
            employeeList.forEach(ansattObject -> {
                ansattObject.getArbeidsforhold().forEach(arbeidsforholdType -> {
                    if (arbeidsforholdType.getOrgUnitId().equals(org.getOrgUnitId())) {

                        relations.add(new Relation.Builder()
                                .with(Organisasjonselement.Relasjonsnavn.ARBEIDSFORHOLD)
                                .forType(Arbeidsforhold.class)
                                .path("/administrasjon/personal/arbeidsforhold")
                                .field("systemid")
                                .value(ArbeidsforholSystemIdUtility.getSystemId(ansattObject.getAnsattNummer(), arbeidsforholdType.getArbeidsforholdnummer()))
                                .build()
                        );
                    }
                });
            });
            relations.add(
                    new Relation.Builder()
                            .with(Organisasjonselement.Relasjonsnavn.ORGANISASJON)
                            .forType(Organisasjonselement.class)
                            .path("/administrasjon/organisasjon/organisasjonselement")
                            .field("organisasjonsid")
                            .value(org.getParentGroupId().replace("/", ":"))
                            .build()
            );
            FintResource<Organisasjonselement> fintResource = FintResource.with(organisasjonselement);
            fintResource.setRelasjoner(relations);

            organisasjonselementList.add(fintResource);
        });

        return organisasjonselementList;
    }
}
