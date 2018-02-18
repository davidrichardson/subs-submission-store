package uk.ac.ebi.submission.store.submittable;


import org.springframework.stereotype.Component;


@Component
public class SubmittableOperationControl {

    public boolean isChangeable(Submittable submittable) {
        return (submittable.getStatus() != null && submittable.getStatus().equals("Draft"));
    }
}
