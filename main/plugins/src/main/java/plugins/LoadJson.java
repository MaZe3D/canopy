package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadJson implements IFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            URL url = new URL(new URL("file:"), parameter);
            System.out.println("loading JSON... source: " + url);
            return new ObjectMapper().readTree(url);
        }
        catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

}
