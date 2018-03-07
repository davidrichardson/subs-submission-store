package uk.ac.ebi.submission.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.document.DocumentStatusEnum;
import uk.ac.ebi.submission.store.submission.SubmissionStatusEnum;

import java.util.*;


@Configuration
public class StatusConfiguration {


    @Bean
    public Map<String, StatusDescription> submissionStatusDescriptionMap() {
        return statusListToMapKeyedOnName(submissionStatuses());
    }

    @Bean
    public Map<String, StatusDescription> documentStatusDescriptionMap() {
        return statusListToMapKeyedOnName(processingStatuses());
    }

    @Bean
    public List<StatusDescription> submissionStatuses() {
        List<StatusDescription> statuses = Arrays.asList(
                StatusDescription.build(SubmissionStatusEnum
                        .Draft, "In preparation")
                        .addUserTransition(SubmissionStatusEnum.Submitted)
                        .acceptUpdates(),

                StatusDescription.build(SubmissionStatusEnum.Submitted, "User has submitted documents for storage by archives")
                        .addSystemTransition(SubmissionStatusEnum.Processing),

                StatusDescription.build(SubmissionStatusEnum.Processing, "Submission system is processing the submission")
                        .addSystemTransition(SubmissionStatusEnum.Completed),

                StatusDescription.build(SubmissionStatusEnum.Completed, "Submission has been stored in the archives")
        );

        return Collections.unmodifiableList(statuses);
    }

    

    @Bean
    public List<StatusDescription> processingStatuses() {
        List<StatusDescription> statuses = Arrays.asList(

                StatusDescription.build(DocumentStatusEnum.Draft, "In preparation")
                        .addUserTransition(DocumentStatusEnum.Submitted)
                        .acceptUpdates(),

                StatusDescription.build(DocumentStatusEnum.Submitted, "User has submitted document for storage by archives")
                        .addSystemTransition(DocumentStatusEnum.Dispatched),

                StatusDescription.build(DocumentStatusEnum.Dispatched, "Submission system has dispatched document to the archive")
                        .addSystemTransition(DocumentStatusEnum.Received),

                StatusDescription.build(DocumentStatusEnum.Received, "Archive has received document")
                        .addSystemTransition(DocumentStatusEnum.Curation)
                        .addSystemTransition(DocumentStatusEnum.Processing),

                StatusDescription.build(DocumentStatusEnum.Curation, "Curation team is reviewing document")
                        .addSystemTransition(DocumentStatusEnum.Accepted)
                        .addSystemTransition(DocumentStatusEnum.ActionRequired),

                StatusDescription.build(DocumentStatusEnum.Accepted, "Curation team has accepted document")
                        .addSystemTransition(DocumentStatusEnum.Processing),

                StatusDescription.build(DocumentStatusEnum.ActionRequired, "Curation team have requested changes or additional information")
                        .addUserTransition(DocumentStatusEnum.Submitted)
                        .acceptUpdates(),

                StatusDescription.build(DocumentStatusEnum.Processing, "Archive is processing document")
                        .addSystemTransition(DocumentStatusEnum.Completed),

                StatusDescription.build(DocumentStatusEnum.Completed, "Archive has stored document"),

                StatusDescription.build(DocumentStatusEnum.Error, "Archive agent has rejected a document"),

                StatusDescription.build(DocumentStatusEnum.Rejected, "Archive agent has rejected a document")
        );


        return statuses;
    }

    private Map<String, StatusDescription> statusListToMapKeyedOnName(List<StatusDescription> statusDescriptions) {
        final Map<String, StatusDescription> stringStatusDescriptionMap = new HashMap<>(statusDescriptions.size());

        statusDescriptions.forEach(sd -> stringStatusDescriptionMap.put(sd.getStatusName(), sd));

        return Collections.unmodifiableMap(stringStatusDescriptionMap);
    }

}
