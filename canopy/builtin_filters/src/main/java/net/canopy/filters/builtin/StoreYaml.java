package net.canopy.filters.builtin;

import net.canopy.app.api.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * The StoreJson class implements the IStoreFilter interface and is used to store YAML data to a target location.
 * It converts the provided YAML node into a YAML string using the Jackson ObjectMapper, and then writes the string to either a file or the standard output, based on the specified parameter.
 */
public class StoreYaml implements IFilter.IStoreFilter {

    private Logger logger = new Logger(this.getClass().getName());

    /**
    * Applies the YAML storing filter to the provided YAML node using the specified parameter.
    *
    * @param jsonNode   The JSON node to apply the filter on.
    * @param parameter  The parameter specifying the target location for storing the YAML data. If no parameter is specified, the YAML data is written to the standard output.
    * @return The YAML node itself.
    * @throws FilterException If an error occurs while storing the YAML data.
    */
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) throws FilterException {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            if (parameter == "") {
                String content = yamlMapper.writeValueAsString(jsonNode);
                logger.log("No parameter specified, using standard output as target");
                System.out.print(content);
            } else {
                logger.log("Using parameter as target-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                logger.log("writing YAML to: " + url);
                yamlMapper.writeValue(new File(url.getFile()), jsonNode);
            }
            return jsonNode;
        } catch (Throwable e) {
            throw new FilterException(this, e.getMessage());
        }
    }

}
