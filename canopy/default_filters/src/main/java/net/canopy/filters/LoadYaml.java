package net.canopy.filters;

import net.canopy.app.IFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadYaml implements IFilter.ILoadFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            if (parameter == "") {
                System.err.println("No parameter specified, using standard input as source");
                return new YAMLMapper().readTree(System.in);
            }
            System.err.println("Using parameter as source-path: " + parameter);
            URL url = new URL(new URL("file:"), parameter);
            System.err.println("loading YAML from: " + url);
            return new YAMLMapper().readTree(url);
        } catch (Throwable e) {
            System.err.println(e);
            return null;
        }
    }

}
