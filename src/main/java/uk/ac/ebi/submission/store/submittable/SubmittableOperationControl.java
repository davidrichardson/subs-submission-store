package uk.ac.ebi.submission.store.submittable;


import org.springframework.stereotype.Component;


@Component
public class SubmittableOperationControl {


    //TODO status rules should be broken out into a separate resource

    public boolean isChangeable(Submittable submittable) {
        return (submittable.getStatus() != null && submittable.getStatus().equals("Draft"));
    }
}
