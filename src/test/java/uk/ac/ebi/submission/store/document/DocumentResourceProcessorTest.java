package uk.ac.ebi.submission.store.document;


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
@SpringBootTest(classes = {DocumentResourceProcessor.class})
public class DocumentResourceProcessorTest {

    @Autowired
    private DocumentResourceProcessor documentResourceProcessor;

    @MockBean
    private RepositoryEntityLinks repositoryEntityLinks;

    @MockBean
    private DocumentOperationControl documentOperationControl;

    @MockBean
    private RelProvider relProvider;

    private Document document;
    private Resource<Document> resource;

    private Link submissionLink;
    private Link typeLinkTemplate;
    private Link typeLink;
    private Link validationResultLink;

    private Link validationResultTemplatedLink;

    @Before
    public void buildUp() {
        document = new Document();
        document.setId("DOC_ID");
        document.setDocumentType("TYPE_NAME");
        document.setSubmissionId("SUB_ID");

        resource = new Resource<>(document);

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

        when(documentOperationControl.isChangeable(document)).thenReturn(true);

        Resource<Document> processedResource = documentResourceProcessor.process(resource);

        List<Link> actualLinks = processedResource.getLinks();
        List<Link> expectedLinks = Arrays.asList(
                typeLink,
                submissionLink,
                validationResultLink
        );

        assertThat(actualLinks, equalTo(expectedLinks));
    }
}
