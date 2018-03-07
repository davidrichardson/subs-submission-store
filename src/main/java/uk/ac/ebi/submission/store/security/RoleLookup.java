package uk.ac.ebi.submission.store.security;

import org.springframework.stereotype.Component;

@Component
public class RoleLookup {
    private static final String ADMIN_USER_DOMAIN_NAME = "self.embl-ebi-subs-admin";


    public String adminRole() {
        return ADMIN_USER_DOMAIN_NAME;
    }


}
