package uk.ac.ebi.submission.store.user;

import lombok.Data;
import uk.ac.ebi.tsc.aap.client.model.User;

@Data
public class UserRepresentation {
    private String userName;
    private String email;
    private String userReference;
    private String fullName;

    UserRepresentation(User u){
        this.userName = u.getUserName();
        this.email = u.getEmail();
        this.userReference = u.getUserReference();
        this.fullName = u.getFullName();
    }

}
