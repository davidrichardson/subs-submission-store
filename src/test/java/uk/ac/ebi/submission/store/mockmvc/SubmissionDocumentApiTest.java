package uk.ac.ebi.submission.store.mockmvc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.SubmissionsApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;
import uk.ac.ebi.submission.store.submission.Submission;
import uk.ac.ebi.submission.store.submissionDocument.SubmissionDocument;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.halLinks;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubmissionsApplication.class)
@WithMockUser(username = "usi_admin_user", roles = {TestUserAndTeamNames.TEAM_NAME, TestUserAndTeamNames.ADMIN_TEAM_NAME})
@Category(DocumentationProducer.class)
public class SubmissionDocumentApiTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(DocumentationHelper.SNIPPET_OUTPUT_DIR);

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DocumentationHelper documentationHelper;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockMvcRestDocumentationConfigurer docConfig = documentationHelper.docConfig(restDocumentation);
        this.mockMvc = DocumentationHelper.mockMvc(this.context, docConfig);
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
