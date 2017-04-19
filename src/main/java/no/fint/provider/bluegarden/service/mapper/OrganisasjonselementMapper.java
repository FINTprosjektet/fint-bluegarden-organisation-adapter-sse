package no.fint.provider.bluegarden.service.mapper;

import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.administrasjon.personal.Arbeidsforhold;
import no.fint.model.felles.Identifikator;
import no.fint.model.relation.FintResource;
import no.fint.model.relation.Relation;
import no.fint.provider.bluegarden.soap.AnsattObject;
import no.fint.provider.bluegarden.soap.OrgListItemObject;
import no.fint.provider.bluegarden.utilities.ArbeidsforholdSystemIdUtility;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganisasjonselementMapper {

    public List<FintResource<Organisasjonselement>> convertToResource(List<OrgListItemObject> orgListItemObjects, List<AnsattObject> employeeList) {
        List<FintResource<Organisasjonselement>> organisasjonselementList = new ArrayList<>();

        orgListItemObjects.forEach(org -> {
            Organisasjonselement organisasjonselement = new Organisasjonselement();

            organisasjonselement.setNavn(org.getOrgNavn());
            organisasjonselement.setAktiv(org.isErAktiv());

            Identifikator organisasjonsId = new Identifikator();
            organisasjonsId.setIdentifikatorverdi(org.getGroupId().replace("/", ":"));
            organisasjonselement.setOrganisasjonsId(organisasjonsId);


            Identifikator organisasjonsKode = new Identifikator();
            organisasjonsKode.setIdentifikatorverdi(org.getOrgUnitId());
            organisasjonselement.setOrganisasjonsKode(organisasjonsKode);

            List<Relation> relations = new ArrayList<>();
            /*employeeList.forEach(ansattObject -> {
                ansattObject.getArbeidsforhold().forEach(arbeidsforholdType -> {
                    if (arbeidsforholdType.getOrgUnitId().equals(org.getOrgUnitId())) {

                        relations.add(new Relation.Builder()
                                .with(Organisasjonselement.Relasjonsnavn.ARBEIDSFORHOLD)
                                .forType(Arbeidsforhold.class)
                                .field("systemid")
                                .value(ArbeidsforholdSystemIdUtility.getSystemId(ansattObject.getAnsattNummer(), arbeidsforholdType.getArbeidsforholdnummer()))
                                .build()
                        );
                    }
                });
            });*/
            relations.add(
                    new Relation.Builder()
                            .with(Organisasjonselement.Relasjonsnavn.OVERORDNET)
                            .forType(Organisasjonselement.class)
                            .field("organisasjonsid")
                            .value(org.getParentGroupId().replace("/", ":"))
                            .build()
            );

            FintResource<Organisasjonselement> fintResource = FintResource.with(organisasjonselement).addRelasjoner(relations);
            organisasjonselementList.add(fintResource);
        });

        return organisasjonselementList;
    }
}
