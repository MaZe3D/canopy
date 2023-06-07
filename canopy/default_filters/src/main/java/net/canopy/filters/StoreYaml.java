package net.canopy.filters;

import net.canopy.app.IFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class StoreYaml implements IFilter.IStoreFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            if (parameter == "") {
                String content = yamlMapper.writeValueAsString(jsonNode);
                System.out.println("No parameter specified, using standard output as target");
                System.out.print(content);
            } else {
                System.out.println("Using parameter as target-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                System.out.println("writing JSON to: " + url);
                yamlMapper.writeValue(new File(url.getFile()), jsonNode);
            }
            return jsonNode;
        } catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

}
