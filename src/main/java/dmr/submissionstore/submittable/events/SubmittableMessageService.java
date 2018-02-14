package dmr.submissionstore.submittable.events;

import dmr.submissionstore.common.Event;
import dmr.submissionstore.submittable.Submittable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmittableMessageService {

    @NonNull
    private RabbitMessagingTemplate rabbitMessagingTemplate;


    private String exchangeName = "usi-2"; //TODO move elsewhere

    public void notifyCrudEvent(Submittable submittable, Event event) {
        log.debug("notify {} for {}",event,submittable);

        String routingKey = String.join(".", "subs", "submittable", submittable.getDocumentType(), event.name());

        log.debug("routing key {} for {} {}",routingKey,event,submittable);


        rabbitMessagingTemplate.convertAndSend(exchangeName,routingKey,submittable);
    }


}
