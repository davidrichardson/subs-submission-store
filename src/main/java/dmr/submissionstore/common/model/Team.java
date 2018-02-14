package dmr.submissionstore.common.model;

import lombok.Data;

@Data
public class Team {
    private String name;

    public static Team of(String name){
        Team t = new Team();
        t.name = name;
        return t;
    }
}
