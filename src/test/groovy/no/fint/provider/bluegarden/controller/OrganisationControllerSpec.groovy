package no.fint.provider.bluegarden.controller

import com.github.spock.spring.utils.MockMvcSpecification
import no.fint.model.administrasjon.organisasjon.Organisasjonselement
import no.fint.model.felles.Identifikator
import no.fint.model.relation.FintResource
import no.fint.provider.bluegarden.service.BlueGardenService
import org.springframework.test.web.servlet.MockMvc

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.Matchers.hasSize

class OrganisationControllerSpec extends MockMvcSpecification {
    private OrganisationController controller
    private BlueGardenService blueGardenService
    private MockMvc mockMvc

    void setup() {
        blueGardenService = Mock(BlueGardenService)
        controller = new OrganisationController(blueGardenService: blueGardenService)
        mockMvc = standaloneSetup(controller)
    }

    def "Get all organisasjonselement"() {
        when:
        def response = mockMvc.perform(get('/organisation'))

        then:
        1 * blueGardenService.getOrganisasjonselementList() >> [FintResource.with(new Organisasjonselement(organisasjonsId: new Identifikator(identifikatorverdi: '123')))]
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(1)))
                .andExpect(jsonPath('$[0].resource.organisasjonsId.identifikatorverdi').value(equalTo('123')))
    }

    def "Get organisasjonselement with organisasjonsId"() {
        when:
        def response = mockMvc.perform(get('/organisation').param('organisasjonsId', '123'))

        then:
        1 * blueGardenService.getOrganisasjonselementList() >> [
                FintResource.with(new Organisasjonselement(organisasjonsId: new Identifikator(identifikatorverdi: '123'))),
                FintResource.with(new Organisasjonselement(organisasjonsId: new Identifikator(identifikatorverdi: '234')))
        ]
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(1)))
                .andExpect(jsonPath('$[0].resource.organisasjonsId.identifikatorverdi').value(equalTo('123')))
    }
}
