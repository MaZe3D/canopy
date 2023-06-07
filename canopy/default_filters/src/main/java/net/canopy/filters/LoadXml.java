package net.canopy.filters;

import net.canopy.app.IFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class LoadXml implements IFilter.ILoadFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            if (parameter == "") {
                System.err.println("No parameter specified, using standard input as source");
                return new XmlMapper().readTree(System.in);
            } else {
                System.err.println("Using parameter as source-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                System.err.println("loading XML from: " + url);
                return new XmlMapper().readTree(url);
            }
        } catch (Throwable e) {
            System.err.println(e);
            return null;
        }
    }
}
