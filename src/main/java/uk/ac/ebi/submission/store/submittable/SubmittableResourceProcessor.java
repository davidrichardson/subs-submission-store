package uk.ac.ebi.submission.store.submittable;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittableType.SubmittableType;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import uk.ac.ebi.submission.store.validationResult.ValidationResultRelNames;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmittableResourceProcessor implements ResourceProcessor<Resource<Submittable>> {

    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public Resource<Submittable> process(Resource<Submittable> resource) {

        log.debug("processing resource {}", resource);

        Submittable submittable = resource.getContent();

        addSubmissionLink(resource, submittable);

        addSubmittableTypeLink(resource, submittable);

        addValidationResultLink(resource, submittable);


        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        return resource;
    }

    private void addValidationResultLink(Resource<Submittable> resource, Submittable submittable) {
        Link unexpandedValidationResultLink = repositoryEntityLinks.linkToSearchResource(
                ValidationResult.class,
                ValidationResultRelNames.BY_SUBMITTABLE_ID
        );
        Map<String, String> expansionParams = new HashMap<>();
        expansionParams.put("submittableId", submittable.getId());
        Link validationResultLink = unexpandedValidationResultLink.expand(expansionParams);

        resource.add(validationResultLink);
    }

    private void addSubmittableTypeLink(Resource<Submittable> resource, Submittable submittable) {
        if (submittable.getSubmittableTypeId() != null) {
            Link typeLink = repositoryEntityLinks.linkToSingleResource(SubmittableType.class, submittable.getSubmittableTypeId());
            resource.add(typeLink);
        }
    }

    private void addSubmissionLink(Resource<Submittable> resource, Submittable submittable) {
        if (submittable.getSubmissionId() != null) {
            Link submissionLink = repositoryEntityLinks.linkToSingleResource(Submission.class, submittable.getId());
            resource.add(submissionLink);
        }
    }
}
