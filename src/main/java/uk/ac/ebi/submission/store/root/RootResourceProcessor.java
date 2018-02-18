package uk.ac.ebi.submission.store.root;

import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submittable.Submittable;
import uk.ac.ebi.submission.store.submittableType.SubmittableType;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
@Data
@Slf4j
public class RootResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {


    @NonNull
    private RepositoryEntityLinks repositoryEntityLinks;

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        log.debug("processing repository links resource {}", resource);

        Class[] exposedClasses = new Class[]{
                Submission.class,
                Submittable.class,
                SubmittableType.class,
                ValidationResult.class
        };


        for (Class clazz : exposedClasses) {
            log.debug("adding links for {}",clazz);
            resource.add(
                    templatedSingleResourceLink(clazz)
            );
            resource.add(
                    searchResourceLink(clazz)
            );
        }

        log.debug("processed repository links resource {}", resource);

        resource.getLinks().sort(
                Comparator.comparing(l -> l.getRel())
        );

        return resource;
    }


    private Link templatedSingleResourceLink(Class clazz) {
        Link link = repositoryEntityLinks.linkToSingleResource(clazz, "***");
        String templateText = link.getHref().replace("***", "{id}");
        UriTemplate template = new UriTemplate(templateText);
        return new Link(template, link.getRel());
    }

    private Collection<Link> searchResourceLink(Class clazz) {

        Link collectionLink = repositoryEntityLinks.linkToCollectionResource(clazz).expand();

        Links nativeSearchLinks = repositoryEntityLinks.linksToSearchResources(clazz);
        List<Link> renamedLinks = new ArrayList<>();

        for (Link searchLink : nativeSearchLinks) {
            renamedLinks.add(
                    new Link(
                            searchLink.getTemplate(),
                            collectionLink.getRel() + "-" + searchLink.getRel()
                    )
            );

        }


        return renamedLinks;
    }
}
