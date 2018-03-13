package uk.ac.ebi.submission.store.mockmvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.SubmissionsApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;
import uk.ac.ebi.submission.store.common.model.Team;
import uk.ac.ebi.submission.store.documentType.DocumentType;
import uk.ac.ebi.submission.store.documentType.DocumentTypeMongoRepository;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submission.SubmissionStatusEnum;
import uk.ac.ebi.submission.store.submission.rest.SubmissionMongoRepository;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocumentMongoRepository;
import uk.ac.ebi.submission.store.validationResult.ValidationResultMongoRepository;

import java.io.IOException;
import java.util.stream.Stream;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionsApplication.class)
@WithMockUser(username = "usi_user", roles = {TestUserAndTeamNames.TEAM_NAME, TestUserAndTeamNames.ADMIN_TEAM_NAME})
@Category(DocumentationProducer.class)
public class SubmissionDocumentApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DocumentationHelper.SNIPPET_OUTPUT_DIR);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentationHelper documentationHelper;

    private MockMvc mockMvc;

    @Autowired
    private DocumentTypeMongoRepository documentTypeMongoRepository;

    @Autowired
    private SubmissionMongoRepository submissionMongoRepository;

    @Autowired
    private ValidationResultMongoRepository validationResultMongoRepository;

    @Autowired
    private SubmissionDocumentMongoRepository submissionDocumentMongoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @MockBean
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    private final DocumentType documentType = new DocumentType();
    private final Submission submission = new Submission();

    @Before
    public void setUp() {
        clearDbs();
        MockMvcRestDocumentationConfigurer docConfig = documentationHelper.docConfig(restDocumentation);
        this.mockMvc = DocumentationHelper.mockMvc(this.context, docConfig);

        //submission
        submission.setTeam(Team.of(TestUserAndTeamNames.TEAM_NAME));
        submission.setStatus(SubmissionStatusEnum.Draft);
        submissionMongoRepository.insert(submission);

        //documenttyoe
        documentType.setTypeName("simplifiedSamples");
        documentTypeMongoRepository.insert(documentType);
    }

    @After
    public void clearDbs() {
        Stream.of(
                documentTypeMongoRepository,
                submissionMongoRepository,
                validationResultMongoRepository,
                submissionDocumentMongoRepository
        ).forEach(r -> r.deleteAll());
    }

    @Test
    public void document_lifecycle() throws Exception {
        SubmissionDocument submissionDocument = submissionDocument();

        String json = objectMapper.writeValueAsString(submissionDocument);

        Link link = entityLinks.linkToCollectionResource(SubmissionDocument.class).expand();

        this.mockMvc.perform(
                post(link.getHref())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(json)
        ).andExpect(status().isCreated())
                .andDo(
                        document("create-one-submission-document",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("curies").ignored(),
                                        linkWithRel("self").ignored(),
                                        linkWithRel("subs:submissionDocument").ignored(),
                                        linkWithRel("subs:documentType").description("The document type for this resource"),
                                        linkWithRel("subs:submission").description("The submission this resource forms part of"),
                                        linkWithRel("subs:validationResult").description("The validation results for this resource")
                                ),
                                responseFields(
                                        fieldWithPath("id").description(""),
                                        subsectionWithPath("team").description("the team that owns this submission document"),
                                        fieldWithPath("documentType").description(""),
                                        fieldWithPath("status").description(""),
                                        fieldWithPath("submissionId").description(""),
                                        subsectionWithPath("content").description(""),
                                        DocumentationHelper.linksResponseField(),
                                        subsectionWithPath("_actions").description(""),
                                        subsectionWithPath("_audit").description(""),
                                        subsectionWithPath("_refs").description(""),
                                        subsectionWithPath("_uploadedFileRefs").description("")
                                )
                        )
                );

        // update one
        SubmissionDocument submissionDocumentInDb = submissionDocumentMongoRepository.findAll().get(0);
        submissionDocument.setId(submissionDocumentInDb.getId());

        json = objectMapper.writeValueAsString(submissionDocument);
        Link updateLink = entityLinks.linkToSingleResource(submissionDocument);


        this.mockMvc.perform(
                put(updateLink.getHref())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(json)
        ).andExpect(status().isOk())
                .andDo(
                        document("update-one-submission-document",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("curies").ignored(),
                                        linkWithRel("self").ignored(),
                                        linkWithRel("subs:submissionDocument").ignored(),
                                        linkWithRel("subs:documentType").description("The document type for this resource"),
                                        linkWithRel("subs:submission").description("The submission this resource forms part of"),
                                        linkWithRel("subs:validationResult").description("The validation results for this resource")
                                ),
                                responseFields(
                                        fieldWithPath("id").description(""),
                                        subsectionWithPath("team").description("the team that owns this submission document"),
                                        fieldWithPath("documentType").description(""),
                                        fieldWithPath("status").description(""),
                                        fieldWithPath("submissionId").description(""),
                                        subsectionWithPath("content").description(""),
                                        DocumentationHelper.linksResponseField(),
                                        subsectionWithPath("_actions").description(""),
                                        subsectionWithPath("_audit").description(""),
                                        subsectionWithPath("_refs").description(""),
                                        subsectionWithPath("_uploadedFileRefs").description("")
                                )
                        )
                );

        // patch one submission document
        String patchJson = "{\n" +
                "  \"content\": {\n" +
                "    \"description\": \"A really excellent sample\"\n" +
                "  }\n" +
                "}";
        this.mockMvc.perform(
                patch(updateLink.getHref())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(patchJson)
        ).andExpect(status().isOk())
                .andDo(
                        document("patch-one-submission-document",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("curies").ignored(),
                                        linkWithRel("self").ignored(),
                                        linkWithRel("subs:submissionDocument").ignored(),
                                        linkWithRel("subs:documentType").description("The document type for this resource"),
                                        linkWithRel("subs:submission").description("The submission this resource forms part of"),
                                        linkWithRel("subs:validationResult").description("The validation results for this resource")
                                ),
                                responseFields(
                                        fieldWithPath("id").description(""),
                                        subsectionWithPath("team").description("the team that owns this submission document"),
                                        fieldWithPath("documentType").description(""),
                                        fieldWithPath("status").description(""),
                                        fieldWithPath("submissionId").description(""),
                                        subsectionWithPath("content").description(""),
                                        DocumentationHelper.linksResponseField(),
                                        subsectionWithPath("_actions").description(""),
                                        subsectionWithPath("_audit").description(""),
                                        subsectionWithPath("_refs").description(""),
                                        subsectionWithPath("_uploadedFileRefs").description("")
                                )
                        )
                );

        // delete one submission document
        this.mockMvc.perform(
                delete(updateLink.getHref())
                        .accept(MediaTypes.HAL_JSON)
        ).andExpect(status().isNoContent())
                .andDo(
                        document("delete-one-submission-document",
                                preprocessRequest(prettyPrint(), DocumentationHelper.addAuthTokenHeader()),
                                preprocessResponse(prettyPrint())
                        )
                );


    }

    private SubmissionDocument submissionDocument() throws IOException {
        SubmissionDocument submissionDocument = new SubmissionDocument();
        submissionDocument.setDocumentType(documentType.getTypeName());
        submissionDocument.setSubmissionId(submission.getId());


        String jsonContent = "{\n" +
                "  \"uniqueName\": \"example-sample\",\n" +
                "  \"description\": \"An example sample\",\n" +
                "  \"taxon\": {\n" +
                "    \"id\": 9606,\n" +
                "    \"scientificName\": \"Homo sapiens\",\n" +
                "    \"commonName\": \"human\"\n" +
                "  }\n" +
                "}";
        JsonNode jsonNode = objectMapper.readTree(jsonContent);

        submissionDocument.setContent(jsonNode);
        return submissionDocument;
    }

    @Test
    public void submission_document_schema() throws Exception {
        this.mockMvc.perform(
                get(documentationHelper.collectionProfile(SubmissionDocument.class))
                        .accept(DocumentationHelper.JSON_SCHEMA_CONTENT_TYPE)
        ).andExpect(status().isOk())
                .andDo(
                        document("submission-document-schema",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks()
                                ),
                                responseFields(
                                        fieldWithPath("title").ignored(),
                                        fieldWithPath("description").ignored(),
                                        fieldWithPath("type").ignored(),
                                        fieldWithPath("$schema").ignored(),
                                        subsectionWithPath("properties").ignored(),
                                        subsectionWithPath("definitions").ignored()
                                )
                        )
                );
    }

}
