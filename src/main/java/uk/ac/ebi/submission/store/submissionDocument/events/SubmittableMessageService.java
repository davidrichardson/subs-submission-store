package uk.ac.ebi.submission.store.submissionDocument.events;

import uk.ac.ebi.submission.messaging.Exchange;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
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


    private String exchangeName = Exchange.EXCHANGE_NAME;

    public void notifyCrudEvent(SubmissionDocument submissionDocument, CrudEvent event) {
        log.debug("notify {} for {}",event, submissionDocument);

        String routingKey = String.join(".", "subs", "submissionDocument", submissionDocument.getDocumentType(), event.name());

        log.debug("routing key {} for {} {}",routingKey,event, submissionDocument);


        rabbitMessagingTemplate.convertAndSend(exchangeName,routingKey, submissionDocument);
    }


}
