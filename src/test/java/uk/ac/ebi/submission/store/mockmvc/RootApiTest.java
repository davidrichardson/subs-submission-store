package uk.ac.ebi.submission.store.mockmvc;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.ac.ebi.submission.DocumentationProducer;
import uk.ac.ebi.submission.store.SubsSubmissionStoreApplication;
import uk.ac.ebi.submission.store.TestUserAndTeamNames;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SubsSubmissionStoreApplication.class)
@WithMockUser(username = "usi_admin_user", roles = {TestUserAndTeamNames.TEAM_NAME, TestUserAndTeamNames.ADMIN_TEAM_NAME})
@Category(DocumentationProducer.class)
public class RootApiTest {

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
    public void rootEndpoint() throws Exception {

        this.mockMvc.perform(
                get(DocumentationHelper.CONTEXT_PATH)
                        .accept(RestMediaTypes.HAL_JSON)
        ).andExpect(status().isOk())
                .andDo(
                        document("api-root",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                links(
                                        halLinks(),
                                        linkWithRel("subs:aapApiRoot").description("Link out to the Authorization, Authentication and Profile API"),
                                        //linkWithRel("subs:archivedDocuments").description("Collection resource for archived documents"),
                                        linkWithRel("subs:checklists").description("Collection resource for submission checklists"),
                                        linkWithRel("subs:documents").description("Collection resource for documents"),
                                        linkWithRel("subs:documentTypes").description("Collection resource for document type information"),
                                        linkWithRel("subs:submissions").description("Collection resource for submissions"),
                                        linkWithRel("subs:submissionPlanWizards").description("Collection resource for the UI wizard for submission construction"),
                                        linkWithRel("subs:validationResults").description("Collection resource for validation results"),
                                        //profile
                                        linkWithRel("profile").description("Application level details"),
                                        linkWithRel("curies").description("CURIE (Compact URI) for expanding to find rel documentation")
                                ),
                                responseFields(

                                        subsectionWithPath("_links").description("<<_links,Links>> to other resources")
//                                        DocumentationHelper.linksResponseField()
                                )
                        )
                );
    }
}
