package no.fint.provider.bluegarden.service;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import no.fint.event.model.Event;
import no.fint.event.model.EventUtil;
import no.fint.event.model.Status;
import no.fint.model.administrasjon.organisasjon.Organisasjonselement;
import no.fint.model.relation.FintResource;
import no.fint.provider.adapter.Health;
import no.fint.provider.adapter.service.EventResponseService;
import no.fint.provider.adapter.service.EventStatusService;
import no.fint.provider.bluegarden.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventHandlerService {

    @Autowired
    private EventResponseService eventResponseService;

    @Autowired
    private EventStatusService eventStatusService;

    @Autowired
    private BlueGardenService blueGardenService;


    public void handleEvent(String event) {
        Event eventObj = EventUtil.toEvent(event);
        if (eventObj != null && eventStatusService.verifyEvent(eventObj).getStatus() == Status.PROVIDER_ACCEPTED) {
            Action action = Action.valueOf(eventObj.getAction());
            Event<?> responseEvent = null;

            if (action == Action.HEALTH) {
                responseEvent = onHealthCheck(event);
            }

            if (action == Action.GET_ALL_ORGANISASJONSELEMENT) {
                responseEvent = onGetAllOrganisasjonselement(event);
            }


            if (responseEvent != null) {
                responseEvent.setStatus(Status.PROVIDER_RESPONSE);
                eventResponseService.postResponse(responseEvent);
            }
        }
    }

    private Event<?> onGetAllOrganisasjonselement(String event) {
        Event<FintResource<Organisasjonselement>> organisasjonselementEvent = EventUtil.toEvent(event);

        organisasjonselementEvent.setData(blueGardenService.getOrganisasjonselementList());

        return organisasjonselementEvent;
    }

    public Event onHealthCheck(String event) {
        Event<String> healthEvent = EventUtil.toEvent(event);

        if (healthCheck()) {
            healthEvent.setData(Lists.newArrayList(Health.APPLICATION_HEALTHY.name()));
        } else {
            healthEvent.setData(Lists.newArrayList(Health.APPLICATION_NOT_HEALTHY.name()));
            healthEvent.setMessage("The adapter is unable to communicate with the application.");
        }

        return healthEvent;
    }

    private boolean healthCheck() {

        if (blueGardenService.getOrganisationStructure().size() > 0) {
            return true;
        }
        return false;
    }
}
