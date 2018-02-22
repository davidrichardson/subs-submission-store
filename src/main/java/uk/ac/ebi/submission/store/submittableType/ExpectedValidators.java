package uk.ac.ebi.submission.store.submittableType;

import lombok.Data;

import java.util.Set;

@Data
public class ExpectedValidators {
    /***
     *  All these named validators must pass for the submittable to be considered valid.
     *
     *  e.g. ENA Experiments must go through the enaExperiment validator
     */
    private Set<String> requiredValidators;


    /**
     * These named validators can pass or fail, it's OK either way
     * <p>
     * e.g. Samples can be created without passing the enaSamples validator, but we always want to know if they
     * pass or fail this check
     */
    private Set<String> informationalValidators;
}
