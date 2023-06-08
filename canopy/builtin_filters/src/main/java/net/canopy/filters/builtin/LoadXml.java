package net.canopy.filters.builtin;

import net.canopy.app.api.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * The LoadJson class implements the ILoadFilter interface and is used to load XML data from a source.
 * It reads XML data from either a file or the standard input, based on the specified parameter.
 */
public class LoadXml implements IFilter.ILoadFilter {

    private Logger logger = new Logger(this.getClass().getName());

    /**
    * Applies the XML loading filter to the provided XML node using the specified parameter.
    *
    * @param jsonNode   The JSON node to apply the filter on.
    * @param parameter  The parameter specifying the source of the XML data. If empty, the standard input is used.
    * @return The loaded XML data as a JsonNode object.
    * @throws FilterException If an error occurs while loading the XML data.
    */
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) throws FilterException {
        try {
            if (parameter == "") {
                logger.log("No parameter specified, using standard input as source");
                return new XmlMapper().readTree(System.in);
            } else {
                logger.log("Using parameter as source-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                logger.log("loading XML from: " + url);
                return new XmlMapper().readTree(url);
            }
        } catch (Throwable e) {
            throw new FilterException(this, e.getMessage());
        }
    }
}
