package uk.ac.ebi.submission.store.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.rest.SubmissionController;
import uk.ac.ebi.submission.store.submission.rest.SubmissionSearchRelNames;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class SearchResourceProcessor implements ResourceProcessor<RepositorySearchesResource> {


    @Override
    public RepositorySearchesResource process(RepositorySearchesResource repositorySearchesResource) {

        final Class domainType = repositorySearchesResource.getDomainType();

        if (Submission.class.equals(domainType)) {

            final Link userSubmissions = ControllerLinkBuilder.linkTo(
                    methodOn(SubmissionController.class)
                            .userSubmissions(null)

            ).withRel(SubmissionSearchRelNames.USER);

            repositorySearchesResource.add(userSubmissions);

        }


        return repositorySearchesResource;
    }
}

