package uk.ac.ebi.submission.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocumentStatusEnum;
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

                StatusDescription.build(SubmissionDocumentStatusEnum.Draft, "In preparation")
                        .addUserTransition(SubmissionDocumentStatusEnum.Submitted)
                        .acceptUpdates(),

                StatusDescription.build(SubmissionDocumentStatusEnum.Submitted, "User has submitted submissionDocument for storage by archives")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Dispatched),

                StatusDescription.build(SubmissionDocumentStatusEnum.Dispatched, "Submission system has dispatched submissionDocument to the archive")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Received),

                StatusDescription.build(SubmissionDocumentStatusEnum.Received, "Archive has received submissionDocument")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Curation)
                        .addSystemTransition(SubmissionDocumentStatusEnum.Processing),

                StatusDescription.build(SubmissionDocumentStatusEnum.Curation, "Curation team is reviewing submissionDocument")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Accepted)
                        .addSystemTransition(SubmissionDocumentStatusEnum.ActionRequired),

                StatusDescription.build(SubmissionDocumentStatusEnum.Accepted, "Curation team has accepted submissionDocument")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Processing),

                StatusDescription.build(SubmissionDocumentStatusEnum.ActionRequired, "Curation team have requested changes or additional information")
                        .addUserTransition(SubmissionDocumentStatusEnum.Submitted)
                        .acceptUpdates(),

                StatusDescription.build(SubmissionDocumentStatusEnum.Processing, "Archive is processing submissionDocument")
                        .addSystemTransition(SubmissionDocumentStatusEnum.Completed),

                StatusDescription.build(SubmissionDocumentStatusEnum.Completed, "Archive has stored submissionDocument"),

                StatusDescription.build(SubmissionDocumentStatusEnum.Error, "Archive agent has rejected a submissionDocument"),

                StatusDescription.build(SubmissionDocumentStatusEnum.Rejected, "Archive agent has rejected a submissionDocument")
        );


        return statuses;
    }

    private Map<String, StatusDescription> statusListToMapKeyedOnName(List<StatusDescription> statusDescriptions) {
        final Map<String, StatusDescription> stringStatusDescriptionMap = new HashMap<>(statusDescriptions.size());

        statusDescriptions.forEach(sd -> stringStatusDescriptionMap.put(sd.getStatusName(), sd));

        return Collections.unmodifiableMap(stringStatusDescriptionMap);
    }

}
