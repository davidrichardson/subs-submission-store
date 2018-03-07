package uk.ac.ebi.submission.store.security;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole(@roleLookup.adminRole(),#entity.team.name)")
public @interface PreAuthorizeDocumentTeamName {
}
