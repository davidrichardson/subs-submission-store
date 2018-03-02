package uk.ac.ebi.submission.store.common.model;

import lombok.Data;

import java.time.Instant;

@Data
public class AuditDetails {

    public AuditDetails(Audited audited){
        this.createdAt = audited.getCreatedAt();
        this.createdBy = audited.getCreatedBy();
        this.lastModifiedAt = audited.getLastModifiedAt();
        this.lastModifiedBy = audited.getLastModifiedBy();
        audited.nullAuditFields();
    }

    private String createdBy;
    private String lastModifiedBy;
    private Instant createdAt;
    private Instant lastModifiedAt;
}
