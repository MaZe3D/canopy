package net.canopy.filters.builtin;

import net.canopy.app.api.IFilter;
import net.canopy.app.api.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadJson implements IFilter.ILoadFilter {

    private Logger logger = new Logger(this.getClass().getName());
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            if (parameter == "") {
                logger.log("No parameter specified, using standard input as source");
                return new ObjectMapper().readTree(System.in);
            }
            logger.log("Using parameter as source-path: " + parameter);
            URL url = new URL(new URL("file:"), parameter);
            logger.log("loading JSON from: " + url);
            return new ObjectMapper().readTree(url);

        } catch (Throwable e) {
            logger.log(e.getMessage());
            return null;
        }
    }


}
