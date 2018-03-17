package uk.ac.ebi.submission.store.common.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class StatusDescription<T extends Enum> {

    @NonNull
    private T status;
    @NonNull
    private String description;
    @Singular
    private Set<T> systemTransitions = new TreeSet<>();
    @Singular
    private Set<T> userTransitions = new TreeSet<>();
    @Builder.Default
    private boolean acceptingUpdates = false;
}
