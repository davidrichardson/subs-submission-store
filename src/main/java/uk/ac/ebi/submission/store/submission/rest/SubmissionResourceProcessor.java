package uk.ac.ebi.submission.store.submission.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionResourceProcessor implements ResourceProcessor<Resource<Submission>> {

    @NonNull
    public SubmissionOperationControl submissionOperationControl;


    @Override
    public Resource<Submission> process(Resource<Submission> resource) {
        log.debug("Converting resource {}", resource);

        SubmissionResource submissionResource = new SubmissionResource(resource);

        if (submissionOperationControl.isChangeable(resource.getContent())) {
            submissionResource.getActions().setDeleteable(true);
            submissionResource.getActions().setUpdateable(true);
        }

        submissionResource.getActions().getStatuses().addAll(
                submissionOperationControl.availableStatuses(resource.getContent())
        );


        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        log.debug("Converted resource to {}", submissionResource);

        return submissionResource;
    }
}
