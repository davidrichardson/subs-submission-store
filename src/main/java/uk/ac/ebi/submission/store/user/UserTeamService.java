package uk.ac.ebi.submission.store.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uk.ac.ebi.submission.store.security.RoleLookup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTeamService {

    @NonNull
    private RoleLookup roleLookup;

    public List<String> userTeams() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> teamList = new ArrayList<>();

        if (authentication != null && authentication.getAuthorities().size() > 0) {
            teamList = authentication.getAuthorities().stream().map(a ->
                    a.getAuthority().toString().replaceFirst("ROLE_", "")
            ).collect(Collectors.toList());

        }
        return teamList;
    }

    public boolean userIsAdmin() {
        return userTeams().contains(roleLookup.adminRole());
    }
}
