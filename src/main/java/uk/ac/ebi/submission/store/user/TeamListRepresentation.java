package uk.ac.ebi.submission.store.user;

import lombok.Data;
import uk.ac.ebi.submission.store.common.model.Team;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TeamListRepresentation {

    public TeamListRepresentation(List<String> teamNames) {
        this.teams = teamNames.stream()
                .sorted()
                .map(Team::of)
                .collect(Collectors.toList());
    }

    private List<Team> teams;


}
