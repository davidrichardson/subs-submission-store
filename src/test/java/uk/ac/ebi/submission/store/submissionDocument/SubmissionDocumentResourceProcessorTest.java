package uk.ac.ebi.submission.store.submissionDocument;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.UriTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.submission.store.documentType.DocumentTypeSearchRelNames;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.validationResult.ValidationResult;
import uk.ac.ebi.submission.store.validationResult.ValidationResultRelNames;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SubmissionDocumentResourceProcessor.class})
public class SubmissionDocumentResourceProcessorTest {

    @Autowired
    private SubmissionDocumentResourceProcessor submissionDocumentResourceProcessor;

    @MockBean
    private RepositoryEntityLinks repositoryEntityLinks;

    @MockBean
    private SubmissionDocumentOperationControl submissionDocumentOperationControl;

    @MockBean
    private RelProvider relProvider;

    private SubmissionDocument submissionDocument;
    private Resource<SubmissionDocument> resource;

    private Link submissionLink;
    private Link typeLinkTemplate;
    private Link typeLink;
    private Link validationResultLink;

    private Link validationResultTemplatedLink;

    @Before
    public void buildUp() {
        submissionDocument = new SubmissionDocument();
        submissionDocument.setId("DOC_ID");
        submissionDocument.setDocumentType("TYPE_NAME");
        submissionDocument.setSubmissionId("SUB_ID");

        resource = new Resource<>(submissionDocument);

        submissionLink = new Link("/submissions/SUB_ID", "submission");
        typeLinkTemplate = new Link("/documentTypes/search/findOneByTypeName{?typeName}", "typeName");
        typeLink = new Link("/documentTypes/search/findOneByTypeName?typeName=TYPE_NAME", "documentType");

        validationResultTemplatedLink = new Link(new UriTemplate("/validationResults/search/findOneBySubmittableId{?submittableId}"), "validationResult");
        validationResultLink = new Link("/validationResults/search/findOneBySubmittableId?submittableId=DOC_ID", "validationResult");
    }


    @Test
    public void testLinks() {

        when(repositoryEntityLinks.linkToSingleResource(Submission.class, "SUB_ID")).thenReturn(submissionLink);
        when(repositoryEntityLinks.linkToSearchResource(DocumentType.class, DocumentTypeSearchRelNames.FIND_ONE_BY_NAME)).thenReturn(typeLinkTemplate);
        when(repositoryEntityLinks.linkToSearchResource(ValidationResult.class, ValidationResultRelNames.BY_DOCUMENT_ID)).thenReturn(validationResultTemplatedLink);
        when(relProvider.getItemResourceRelFor(DocumentType.class)).thenReturn("documentType");

        when(submissionDocumentOperationControl.isChangeable(submissionDocument)).thenReturn(true);

        Resource<SubmissionDocument> processedResource = submissionDocumentResourceProcessor.process(resource);

        List<Link> actualLinks = processedResource.getLinks();
        List<Link> expectedLinks = Arrays.asList(
                typeLink,
                submissionLink,
                validationResultLink
        );

        assertThat(actualLinks, equalTo(expectedLinks));
    }
}
