package net.canopy.filters;

import net.canopy.app.IFilter;
import net.canopy.app.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class StoreJson implements IFilter.IStoreFilter {

    private Logger logger = new Logger(this.getClass().getName());

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            String content = objectMapper.writeValueAsString(jsonNode);

            if (parameter == "") {
                logger.log("No parameter specified, using standard output as target");
                System.out.print(content);
            } else {
                logger.log("Using parameter as target-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                logger.log("writing JSON to: " + url);
                objectMapper.writeValue(new File(url.getFile()), jsonNode);
            }
            return jsonNode;
        }
        catch (Throwable e) {
            logger.log(e.getMessage());
            return null;
        }
    }
}
