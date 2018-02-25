package uk.ac.ebi.submission.dispatch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessingCertificateEnvelope {

    private String submissionId;

    private final Collection<ProcessingCertificate> processingCertificates = new ArrayList<>();

    public void addProcessingCertificate(ProcessingCertificate processingCertificate) {
        processingCertificates.add(processingCertificate);
    }
}