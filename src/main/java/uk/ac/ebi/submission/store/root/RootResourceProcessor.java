package uk.ac.ebi.submission.store.root;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;

@Component
@Data
@Slf4j
public class RootResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {


    @Value("${aap.domains.url:https://explore.api.aai.ebi.ac.uk}")
    private String aapApiRootUrl;

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        log.debug("processing repository links resource {}", resource);

        resource.add(new Link(aapApiRootUrl, "aapApiRoot"));

        log.debug("processed repository links resource {}", resource);

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(resource);

        return resource;
    }
}
