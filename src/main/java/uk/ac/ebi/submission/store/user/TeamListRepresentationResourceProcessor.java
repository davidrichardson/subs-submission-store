package uk.ac.ebi.submission.store.user;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class TeamListRepresentationResourceProcessor implements ResourceProcessor<Resource<TeamListRepresentation>> {
    @Override
    public Resource<TeamListRepresentation> process(Resource<TeamListRepresentation> resource) {

        Link selfLink = linkTo(methodOn(UserController.class).currentUserTeams())
                .withSelfRel();

        Link teamsLink = selfLink.withRel("teams");


        resource.add(
                selfLink,
                teamsLink
        );

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        return resource;
    }
}
