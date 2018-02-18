package uk.ac.ebi.submission.store.common.model;

import lombok.Data;

@Data
public class Submitter {
    private String email;
    private String name;

    public static Submitter of(String email, String name) {
        Submitter s = new Submitter();
        s.email = email;
        s.name = name;
        return s;
    }
}
