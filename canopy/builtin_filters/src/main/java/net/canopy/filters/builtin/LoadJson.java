
package net.canopy.filters.builtin;

import net.canopy.app.api.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * The LoadJson class implements the ILoadFilter interface and is used to load JSON data from a source.
 * It reads JSON data from either a file or the standard input, based on the specified parameter.
 */
public class LoadJson implements IFilter.ILoadFilter {

    private Logger logger = new Logger(this.getClass().getName());
    @Override
    /**
    * Applies the JSON loading filter to the provided JSON node using the specified parameter.
    *
    * @param jsonNode   The JSON node to apply the filter on.
    * @param parameter  The parameter specifying the source of the JSON data. If empty, the standard input is used.
    * @return The loaded JSON data as a JsonNode object.
    * @throws FilterException If an error occurs while loading the JSON data.
    */
    public JsonNode apply(JsonNode jsonNode, String parameter) throws FilterException {
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
            throw new FilterException(this, e.getMessage());
        }
    }
}
