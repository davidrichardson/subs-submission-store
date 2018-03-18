package uk.ac.ebi.submission.store.root;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import uk.ac.ebi.submission.store.common.ResourceLinkHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Data
@Slf4j
public class RootResourceProcessor implements ResourceProcessor<RepositoryLinksResource> {


    @Value("${aap.domains.url:https://explore.api.aai.ebi.ac.uk}")
    private String aapApiRootUrl;

    /* use this to clear standard optional parameters that are not applicable for the rel , e.g. aggregate roots with
      no data, just links
     */
    private final Set<String> relsToExpand = getRelsToExpand();

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        RepositoryLinksResource repositoryLinksResource = new RepositoryLinksResource();

        log.debug("processing repository links resource {}", resource);

        repositoryLinksResource.add(new Link(aapApiRootUrl, "aapApiRoot"));

        for (Link link : resource.getLinks()) {
            if (relsToExpand.contains(link.getRel())) {
                repositoryLinksResource.add(link.expand());
            } else {
                repositoryLinksResource.add(link);
            }
        }

        ResourceLinkHelper.sortResourceLinksAlphabeticallyByRelName(repositoryLinksResource);

        log.debug("processed repository links resource {}", repositoryLinksResource);


        return repositoryLinksResource;
    }

    private static Set<String> getRelsToExpand() {
        Set<String> set = new HashSet<>();
        set.addAll(Arrays.asList("submissionDocuments", "submissions", "archivedDocuments", "validationResults"));
        return set;
    }
}
