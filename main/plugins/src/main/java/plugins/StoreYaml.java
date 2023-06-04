package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class StoreYaml implements IFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            YAMLMapper yamlMapper = new YAMLMapper();
            yamlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println(yamlMapper.writeValueAsString(jsonNode));
            return jsonNode;
        }
        catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

}
