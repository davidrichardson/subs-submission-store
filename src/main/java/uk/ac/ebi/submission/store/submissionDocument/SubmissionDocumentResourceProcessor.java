package uk.ac.ebi.submission.store.submissionDocument;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.documentType.DocumentTypeSearchRelNames;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import uk.ac.ebi.submission.store.validationResult.ValidationResultRelNames;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionDocumentResourceProcessor implements ResourceProcessor<Resource<SubmissionDocument>> {

    @NonNull
    private SubmissionDocumentOperationControl submissionDocumentOperationControl;

    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @NonNull
    private RelProvider relProvider;

    @Override
    public Resource<SubmissionDocument> process(Resource<SubmissionDocument> rawSubmittableResource) {

        log.debug("Processing resource {}", rawSubmittableResource);

        SubmissionDocumentResource resource = buildSubmittableResource(rawSubmittableResource);

        addLinks(resource);

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        log.debug("Converted resource to {}", resource);


        return resource;
    }

    private void addLinks(SubmissionDocumentResource resource) {
        SubmissionDocument submissionDocument = resource.getContent();

        addSubmissionLink(resource, submissionDocument);

        addSubmittableTypeLink(resource, submissionDocument);

        addValidationResultLink(resource, submissionDocument);
    }

    private SubmissionDocumentResource buildSubmittableResource(Resource<SubmissionDocument> rawSubmittableResource) {
        SubmissionDocumentResource resource = new SubmissionDocumentResource(rawSubmittableResource);

        if (submissionDocumentOperationControl.isChangeable(resource.getContent())) {
            resource.getActions().setDeleteable(true);
            resource.getActions().setUpdateable(true);
        }

        return resource;
    }

    private void addValidationResultLink(Resource<SubmissionDocument> resource, SubmissionDocument submissionDocument) {
        Link unexpandedValidationResultLink = repositoryEntityLinks.linkToSearchResource(
                ValidationResult.class,
                ValidationResultRelNames.BY_DOCUMENT_ID
        );
        Map<String, String> expansionParams = new HashMap<>();
        expansionParams.put("documentId", submissionDocument.getId());
        Link validationResultLink = unexpandedValidationResultLink.expand(expansionParams)
                .withRel(relProvider.getItemResourceRelFor(ValidationResult.class));

        resource.add(validationResultLink);
    }

    private void addSubmittableTypeLink(Resource<SubmissionDocument> resource, SubmissionDocument submissionDocument) {
        if (submissionDocument.getDocumentType() != null) {
            Link typeLink = repositoryEntityLinks.linkToSearchResource(
                    DocumentType.class,
                    DocumentTypeSearchRelNames.FIND_ONE_BY_NAME
            );

            Map<String, String> expansionParams = new HashMap<>();
            expansionParams.put("typeName", submissionDocument.getDocumentType());

            String rel = relProvider.getItemResourceRelFor(DocumentType.class);

            Link expandedTypeLink = typeLink.expand(expansionParams).withRel(rel);

            resource.add(expandedTypeLink);
        }
    }

    private void addSubmissionLink(Resource<SubmissionDocument> resource, SubmissionDocument submissionDocument) {
        if (submissionDocument.getSubmissionId() != null) {
            Link submissionLink = repositoryEntityLinks.linkToSingleResource(Submission.class, submissionDocument.getSubmissionId());
            resource.add(submissionLink);
        }
    }
}
