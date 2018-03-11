package uk.ac.ebi.submission.store.document;

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
import uk.ac.ebi.submission.store.documentType.DocumentTypeSearchRelNames;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import uk.ac.ebi.submission.store.validationResult.ValidationResultRelNames;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentResourceProcessor implements ResourceProcessor<Resource<Document>> {

    @NonNull
    private DocumentOperationControl documentOperationControl;

    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @NonNull
    private RelProvider relProvider;

    @Override
    public Resource<Document> process(Resource<Document> rawSubmittableResource) {

        log.debug("Processing resource {}", rawSubmittableResource);

        DocumentResource resource = buildSubmittableResource(rawSubmittableResource);

        addLinks(resource);

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        log.debug("Converted resource to {}", resource);


        return resource;
    }

    private void addLinks(DocumentResource resource) {
        Document document = resource.getContent();

        addSubmissionLink(resource, document);

        addSubmittableTypeLink(resource, document);

        addValidationResultLink(resource, document);
    }

    private DocumentResource buildSubmittableResource(Resource<Document> rawSubmittableResource) {
        DocumentResource resource = new DocumentResource(rawSubmittableResource);

        if (documentOperationControl.isChangeable(resource.getContent())) {
            resource.getActions().setDeleteable(true);
            resource.getActions().setUpdateable(true);
        }

        return resource;
    }

    private void addValidationResultLink(Resource<Document> resource, Document document) {
        Link unexpandedValidationResultLink = repositoryEntityLinks.linkToSearchResource(
                ValidationResult.class,
                ValidationResultRelNames.BY_DOCUMENT_ID
        );
        Map<String, String> expansionParams = new HashMap<>();
        expansionParams.put("submittableId", document.getId());
        Link validationResultLink = unexpandedValidationResultLink.expand(expansionParams);

        resource.add(validationResultLink);
    }

    private void addSubmittableTypeLink(Resource<Document> resource, Document document) {
        if (document.getDocumentType() != null) {
            Link typeLink = repositoryEntityLinks.linkToSearchResource(
                    DocumentType.class,
                    DocumentTypeSearchRelNames.FIND_ONE_BY_NAME
            );

            Map<String,String> expansionParams = new HashMap<>();
            expansionParams.put("typeName",document.getDocumentType());

            String rel = relProvider.getItemResourceRelFor(DocumentType.class);

            Link expandedTypeLink = typeLink.expand(expansionParams).withRel(rel);

            resource.add(expandedTypeLink);
        }
    }

    private void addSubmissionLink(Resource<Document> resource, Document document) {
        if (document.getSubmissionId() != null) {
            Link submissionLink = repositoryEntityLinks.linkToSingleResource(Submission.class, document.getSubmissionId());
            resource.add(submissionLink);
        }
    }
}
