package dmr.submissionstore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonHelper {

    public static JsonNode stringToJsonNode(String jsonContent){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;
        try {
            actualObj = mapper.readTree(jsonContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return actualObj;
    }
}
