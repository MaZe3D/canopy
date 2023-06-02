package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class LoadJson implements IFilter {

    public static int dummy(int i) {
        return i + 1;
    }

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        String jsonString = "{ \"name\":\"John\", \"age\":30, \"city\":\"New York\" }";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = null;

        try {
            actualObj = mapper.readTree(jsonString);
            //System.out.println(actualObj.toString());
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return actualObj;
    }

}
