package uk.ac.ebi.submission.store.security;

import org.springframework.security.access.prepost.PostAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("!returnObject.isPresent or hasAnyRole(@roleLookup.adminRole(),returnObject.get.team.name)")
public @interface PostAuthorizeOptionalReturnObjectHasTeamName {
}
