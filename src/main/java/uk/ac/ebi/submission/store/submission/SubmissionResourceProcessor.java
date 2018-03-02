package uk.ac.ebi.submission.store.submission;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

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


        log.debug("Converted resource to {}", submissionResource);

        return submissionResource;
    }
}
