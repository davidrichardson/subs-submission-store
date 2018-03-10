package uk.ac.ebi.submission.store.mockmvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.RelProvider;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.SubsectionDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;

@Component
public class DocumentationHelper {

    @Autowired
    private RelProvider relProvider;

    @Value("${usi.docs.hostname:localhost}")
    private String host;
    @Value("${usi.docs.port:8080}")
    private int port;
    @Value("${usi.docs.scheme:http}")
    private String scheme;


    protected static final String JSON_SCHEMA_CONTENT_TYPE = "application/schema+json";

    public static final String CONTEXT_PATH = "";

    public static final String SNIPPET_OUTPUT_DIR = "build/generated-snippets";

    protected String collectionRoot(Class resourceClass){
        return CONTEXT_PATH + "/" + collectionName(resourceClass);
    }

    private String collectionName(Class resourceClass){
        return relProvider.getCollectionResourceRelFor(resourceClass);
    }

    protected String collectionProfile(Class resourceClass){
        return CONTEXT_PATH + "/profile/" + collectionName(resourceClass);
    }

    protected static JUnitRestDocumentation jUnitRestDocumentation() {
        return new JUnitRestDocumentation("build/generated-snippets");
    }


    protected static ObjectMapper mapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper;

    }

    protected static MockMvc mockMvc(WebApplicationContext context, MockMvcRestDocumentationConfigurer docConfig) {
        return MockMvcBuilders.webAppContextSetup(context)
                .apply(docConfig)
                .defaultRequest(get("/").contextPath(CONTEXT_PATH))
                .build();
    }

    protected MockMvcRestDocumentationConfigurer docConfig(JUnitRestDocumentation restDocumentation) {
        MockMvcRestDocumentationConfigurer docConfig = documentationConfiguration(restDocumentation);

        docConfig.uris()
                .withScheme(scheme)
                .withHost(host)
                .withPort(port);

        return docConfig;
    }

    protected static HeaderAddingPreprocessor addHeader(String headerName, String headerValue) {
        HeaderAddingPreprocessor preprocessor = new HeaderAddingPreprocessor();
        preprocessor.addHeader(headerName, headerValue);
        return preprocessor;
    }

    protected static HeaderAddingPreprocessor addAuthTokenHeader() {
        return DocumentationHelper.addHeader("Authorization", "Bearer $TOKEN");
    }


    protected static FieldDescriptor linksResponseField() {
        return subsectionWithPath("_links").description("Links to other resources");
    }

    protected static LinkDescriptor selfRelLink() {
        return linkWithRel("self").description("Canonical link for this resource");
    }

    protected static FieldDescriptor paginationBlock() {
        return subsectionWithPath("page").description("Pagination information including current page number, size of page, total elements and total pages availablee");
    }

    protected static LinkDescriptor nextRelLink() {
        return linkWithRel("next").description("Next page of this resource");
    }

    protected static LinkDescriptor lastRelLink() {
        return linkWithRel("last").description("Last page for this resource");
    }

    protected static LinkDescriptor firstRelLink() {
        return linkWithRel("first").description("First page for this resource");
    }

    protected static LinkDescriptor prevRelLink() {
        return linkWithRel("prev").description("Previous page for this resource");
    }
}
