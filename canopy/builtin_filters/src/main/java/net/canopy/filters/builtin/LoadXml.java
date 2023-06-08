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

public class LoadXml implements IFilter.ILoadFilter {

    private Logger logger = new Logger(this.getClass().getName());

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
