package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadYaml implements IFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            URL url = new URL(new URL("file:"), parameter);
            System.out.println("loading YAML from: " + url);
            return new YAMLMapper().readTree(url);
        }
        catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

}
