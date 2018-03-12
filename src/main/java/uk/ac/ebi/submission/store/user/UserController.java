package uk.ac.ebi.submission.store.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ebi.tsc.aap.client.model.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    @NonNull
    private CurrentUserService currentUserService;

    @NonNull
    private UserRepresentationResourceProcessor userRepresentationResourceProcessor;

    @NonNull
    private TeamListRepresentationResourceProcessor teamListRepresentationResourceProcessor;

    @RequestMapping(method = RequestMethod.GET, value = "/user")
    public @ResponseBody
    ResponseEntity<Resource<UserRepresentation>> currentUser() {
        User user = currentUserService.getCurrentUser();

        Resource<UserRepresentation> resource = userRepresentationResourceProcessor.process(
                new Resource<>(new UserRepresentation(user))
        );

        return ResponseEntity.ok(resource);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/teams")
    public @ResponseBody
    ResponseEntity<Resource<TeamListRepresentation>> currentUserTeams() {
        List<String> teamNames = currentUserService.userTeams();

        TeamListRepresentation teams = new TeamListRepresentation(teamNames);

        Resource<TeamListRepresentation> resource = teamListRepresentationResourceProcessor.process(new Resource<TeamListRepresentation>(teams));

        return ResponseEntity.ok(resource);
    }


}
