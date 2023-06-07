package net.canopy.filters;

import net.canopy.app.IFilter;

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
            if (parameter == "") {
                System.out.println("No parameter specified, using standard input as source");
                return new ObjectMapper().readTree(System.in);
            }
            System.out.println("Using parameter as source-path: " + parameter);
            URL url = new URL(new URL("file:"), parameter);
            System.out.println("loading JSON from: " + url);
            return new ObjectMapper().readTree(url);

        } catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }
}
