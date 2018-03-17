package uk.ac.ebi.submission.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.ac.ebi.submission.store.common.model.StatusDescription;
import uk.ac.ebi.submission.store.submission.SubmissionStatus;
import uk.ac.ebi.submission.store.submissionDocument.ProcessingStatus;

import java.util.*;


@Configuration
public class StatusConfiguration {


    @Bean
    public Map<SubmissionStatus, StatusDescription<SubmissionStatus>> submissionStatusDescriptionMap() {
        Map<SubmissionStatus, StatusDescription<SubmissionStatus>> statusDescriptionMap = new HashMap<>();

        for (StatusDescription<SubmissionStatus> sd : submissionStatuses()) {
            statusDescriptionMap.put(sd.getStatus(), sd);
        }

        return Collections.unmodifiableMap(statusDescriptionMap);
    }

    @Bean
    public Map<ProcessingStatus, StatusDescription> processingStatusDescriptionMap() {
        Map<ProcessingStatus, StatusDescription<ProcessingStatus>> statusDescriptionMap = new HashMap<>();

        for (StatusDescription<ProcessingStatus> sd : processingStatuses()) {
            statusDescriptionMap.put(sd.getStatus(), sd);
        }

        return Collections.unmodifiableMap(statusDescriptionMap);
    }

    @Bean
    public List<StatusDescription<SubmissionStatus>> submissionStatuses() {
        List<StatusDescription<SubmissionStatus>> statuses = Arrays.asList(

                StatusDescription.<SubmissionStatus>builder()
                        .status(SubmissionStatus.Draft)
                        .description("In preparation")
                        .userTransition(SubmissionStatus.Submitted)
                        .acceptingUpdates(true)
                        .build(),

                StatusDescription.<SubmissionStatus>builder()
                        .status(SubmissionStatus.Submitted)
                        .description("User has submitted documents for storage by archives")
                        .systemTransition(SubmissionStatus.Processing)
                        .build(),

                StatusDescription.<SubmissionStatus>builder()
                        .status(SubmissionStatus.Processing)
                        .description("Submission system is processing the submission")
                        .systemTransition(SubmissionStatus.Completed)
                        .build(),

                StatusDescription.<SubmissionStatus>builder()
                        .status(SubmissionStatus.Completed)
                        .description("Submission has been stored in the archives")
                        .build()
        );

        return Collections.unmodifiableList(statuses);
    }


    @Bean
    public List<StatusDescription<ProcessingStatus>> processingStatuses() {
        List<StatusDescription<ProcessingStatus>> statuses = Arrays.asList(

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Draft)
                        .description("In preparation")
                        .systemTransition(ProcessingStatus.Submitted)
                        .acceptingUpdates(true)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Submitted)
                        .description("User has submitted submissionDocument for storage by archives")
                        .systemTransition(ProcessingStatus.Dispatched)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Dispatched)
                        .description("Submission system has dispatched submissionDocument to the archive")
                        .systemTransition(ProcessingStatus.Received)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Received)
                        .description("Archive has received submissionDocument")
                        .systemTransition(ProcessingStatus.Curation)
                        .systemTransition(ProcessingStatus.Processing)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Curation)
                        .description("Curation team is reviewing submissionDocument")
                        .systemTransition(ProcessingStatus.Accepted)
                        .systemTransition(ProcessingStatus.ActionRequired)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Accepted)
                        .description("Curation team has accepted submissionDocument")
                        .systemTransition(ProcessingStatus.Processing)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.ActionRequired)
                        .description("Curation team have requested changes or additional information")
                        .userTransition(ProcessingStatus.Submitted)
                        .acceptingUpdates(true)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Processing)
                        .description("Archive is processing submissionDocument")
                        .systemTransition(ProcessingStatus.Completed)
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Completed)
                        .description("Archive has stored submissionDocument")
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Error)
                        .description("Archive agent has rejected a submissionDocument")
                        .build(),

                StatusDescription.<ProcessingStatus>builder()
                        .status(ProcessingStatus.Rejected)
                        .description("Archive agent has rejected a submissionDocument")
                        .build()
        );


        return statuses;
    }


}
