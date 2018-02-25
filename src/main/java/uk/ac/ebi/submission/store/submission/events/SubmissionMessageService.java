package uk.ac.ebi.submission.store.submission.events;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.messaging.Exchange;
import uk.ac.ebi.submission.store.common.CrudEvent;
import uk.ac.ebi.submission.store.submission.Submission;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionMessageService {

    @NonNull
    private RabbitMessagingTemplate rabbitMessagingTemplate;


    private String exchangeName = Exchange.EXCHANGE_NAME;

    public void notifyCrudEvent(Submission submission, CrudEvent event) {
        log.debug("notify {} for {}", event, submission);

        String routingKey = String.join(".", "subs", "submission", event.name());

        log.debug("routing key {} for {} {}", routingKey, event, submission);


        rabbitMessagingTemplate.convertAndSend(exchangeName, routingKey, submission);
    }
}
