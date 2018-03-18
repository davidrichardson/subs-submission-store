package uk.ac.ebi.submission.store.submission.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionOperationControl;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.rest.SubmissionDocumentController;
import uk.ac.ebi.submission.store.submissionDocument.rest.SubmissionDocumentSearchRelNames;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubmissionResourceProcessor implements ResourceProcessor<Resource<Submission>> {

    @NonNull
    private SubmissionOperationControl submissionOperationControl;

    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @NonNull
    private RelProvider relProvider;

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

        submissionResource.add(
                linkToSubmissionDocuments(resource.getContent())
        );

        submissionResource.add(
                linkToStatusSummary(resource.getContent())
        );

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(submissionResource);

        log.debug("Converted resource to {}", submissionResource);

        return submissionResource;
    }

    private Link linkToStatusSummary(Submission submission) {
        return linkTo(
                methodOn(SubmissionDocumentController.class)
                        .summariseDocumentStatus(submission.getId())).withRel("documentStatusSummary");
    }

    private Link linkToSubmissionDocuments(Submission submission) {
        Link baseLink = repositoryEntityLinks.linkToSearchResource(SubmissionDocument.class, SubmissionDocumentSearchRelNames.BY_SUBMISSION_ID_AND_DOC_TYPE);

        Map<String, String> expansionParams = new HashMap<>();
        expansionParams.put("submissionId", submission.getId());

        Link expandedLink = partialLinkExpansion(baseLink, expansionParams);

        String relName = relProvider.getCollectionResourceRelFor(SubmissionDocument.class);

        return expandedLink.withRel(relName);
    }

    private Link partialLinkExpansion(Link link, Map<String, String> parameters) {
        UriTemplate partiallyExpandedTemplate = partialExpansion(link.getTemplate(), parameters);

        return new Link(partiallyExpandedTemplate, link.getRel());
    }

    private UriTemplate partialExpansion(UriTemplate uriTemplate, Map<String, String> parameters) {
        URI initialExpansion = uriTemplate.expand(parameters);

        UriTemplate partiallyExpandedTemplate = new UriTemplate(initialExpansion.toString());

        List<TemplateVariable> unexpandedTemplateVariables = new ArrayList<>();

        for (TemplateVariable templateVariable : uriTemplate.getVariables()) {

            if (!parameters.containsKey(templateVariable.getName())) {
                unexpandedTemplateVariables.add(templateVariable);
            }

        }

        if (unexpandedTemplateVariables.isEmpty()) {
            return partiallyExpandedTemplate;
        } else {
            TemplateVariables tv = new TemplateVariables(unexpandedTemplateVariables);
            return partiallyExpandedTemplate.with(tv);
        }
    }
}
