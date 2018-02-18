package uk.ac.ebi.submission.store.common;

import org.springframework.hateoas.ResourceSupport;

import java.util.Comparator;

public class ResourceLinkHelper {

    public static void sortResourceLinksAlphabeticallyByRelName(ResourceSupport resource) {
        resource.getLinks().sort(
                Comparator.comparing(l -> l.getRel())
        );
    }

}
