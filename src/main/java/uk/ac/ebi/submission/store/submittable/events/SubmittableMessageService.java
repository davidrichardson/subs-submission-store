package uk.ac.ebi.submission.store.submittable.events;

import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submittable.Submittable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmittableMessageService {

    @NonNull
    private RabbitMessagingTemplate rabbitMessagingTemplate;


    private String exchangeName = "usi-2"; //TODO move elsewhere

    public void notifyCrudEvent(Submittable submittable, CrudEvent event) {
        log.debug("notify {} for {}",event,submittable);

        String routingKey = String.join(".", "subs", "submittable", submittable.getDocumentType(), event.name());

        log.debug("routing key {} for {} {}",routingKey,event,submittable);


        rabbitMessagingTemplate.convertAndSend(exchangeName,routingKey,submittable);
    }


}
