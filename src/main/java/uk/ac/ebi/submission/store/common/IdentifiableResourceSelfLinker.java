package uk.ac.ebi.submission.store.common;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Identifiable;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentifiableResourceSelfLinker<T extends Identifiable> {

    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @NonNull
    private RelProvider relProvider;

    public Resource<T> addSelfLinks(Resource<T> resource){
        resource.add(
                repositoryEntityLinks.linkForSingleResource(resource.getContent()).withSelfRel(),
                repositoryEntityLinks.linkForSingleResource(resource.getContent()).withRel( relProvider.getItemResourceRelFor(resource.getContent().getClass()))
        );

        return resource;
    }
}