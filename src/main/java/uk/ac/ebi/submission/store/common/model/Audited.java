package uk.ac.ebi.submission.store.common.model;

import java.time.Instant;

public interface Audited {

    String getCreatedBy();
    String getLastModifiedBy();
    Instant getCreatedAt();
    Instant getLastModifiedAt();

    void setCreatedBy(String createdBy);
    void setLastModifiedBy(String lastModifiedBy);
    void setCreatedAt(Instant createdAt);
    void setLastModifiedAt(Instant lastModifiedAt);

    default void nullAuditFields(){
        setCreatedBy(null);
        setLastModifiedBy(null);
        setCreatedAt(null);
        setLastModifiedAt(null);
    }
}
