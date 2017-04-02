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
            FintResource<Organisasjonselement> fintResource = new FintResource<>();
            Organisasjonselement organisasjonselement = new Organisasjonselement();

            organisasjonselement.setNavn(org.getOrgNavn());
            organisasjonselement.setAktiv(org.isErAktiv());

            Identifikator organisasjonsId = new Identifikator();
            organisasjonsId.setIdentifikatorverdi(org.getGroupId());
            organisasjonselement.setOrganisasjonsId(organisasjonsId);


            Identifikator organisasjonsKode = new Identifikator();
            organisasjonsKode.setIdentifikatorverdi(org.getOrgUnitId());
            organisasjonselement.setOrganisasjonsKode(organisasjonsKode);

            fintResource.with(organisasjonselement)
                    .addRelasjon(
                            new Relation.Builder()
                                    .with(Organisasjonselement.Relasjonsnavn.ORGANISASJON)
                                    .forType(Organisasjonselement.class)
                                    .field("orgid")
                                    .value(org.getParentGroupId())
                                    .build()
                    );

            employeeList.forEach(ansattObject -> {
                ansattObject.getArbeidsforhold().forEach(arbeidsforholdType -> {
                    if (arbeidsforholdType.getOrgUnitId().equals(org.getOrgUnitId())) {

                        fintResource.addRelasjon(new Relation.Builder()
                                .with(Organisasjonselement.Relasjonsnavn.ARBEIDSFORHOLD)
                                .forType(Arbeidsforhold.class)
                                .field("systemid")
                                .value(ArbeidsforholSystemIdUtility.getSystemId(ansattObject.getAnsattNummer(), arbeidsforholdType.getArbeidsforholdnummer()))
                                .build()
                        );
                    }
                });
            });

            organisasjonselementList.add(fintResource);
        });

        return organisasjonselementList;
    }
}
