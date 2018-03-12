package uk.ac.ebi.submission.store.user;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserRepresentationResourceProcessor implements ResourceProcessor<Resource<UserRepresentation>> {
    @Override
    public Resource<UserRepresentation> process(Resource<UserRepresentation> resource) {

        Link selfLink = linkTo(methodOn(UserController.class).currentUser())
                .withSelfRel();

        Link userLink = selfLink.withRel("user");

        Link teamsLink = linkTo(methodOn(UserController.class).currentUserTeams()).withRel("teams");

        resource.add(
                selfLink,
                userLink,
                teamsLink
        );

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        return resource;
    }
}
