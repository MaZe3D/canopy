package net.canopy.filters.builtin;

import net.canopy.app.api.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * The StoreJson class implements the IStoreFilter interface and is used to store XML data to a target location.
 * It converts the provided XML node into a XML string using the Jackson ObjectMapper, and then writes the string to either a file or the standard output, based on the specified parameter.
 */
public class StoreXml implements IFilter.IStoreFilter {

    private Logger logger = new Logger(this.getClass().getName());

    /**
    * Applies the XML storing filter to the provided XML node using the specified parameter.
    *
    * @param jsonNode   The JSON node to apply the filter on.
    * @param parameter  The parameter specifying the target location for storing the XML data. If no parameter is specified, the XML data is written to the standard output.
    * @return The XML node itself.
    * @throws FilterException If an error occurs while storing the XML data.
    */
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) throws FilterException {
        try {
            // wrap root element with "row" if it's an array
            JsonNode nodeToStore = jsonNode;
            if (nodeToStore.isArray()) {
                nodeToStore = new ObjectNode(JsonNodeFactory.instance).set("row", nodeToStore);
            }

            SimpleModule module = new SimpleModule();
            module.addSerializer(JsonNode.class, new XmlSpaceToUnderscoreSerializer());

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
            xmlMapper.registerModule(module);

            if (parameter == "") {
                String content = xmlMapper.writeValueAsString(nodeToStore);
                logger.log("No parameter specified, using standard output as target");
                System.out.print(content);
            } else {
                logger.log("Using parameter as target-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                logger.log("writing XML to: " + url);
                xmlMapper.writeValue(new File(url.getFile()), nodeToStore);
            }

            return jsonNode;
        }
        catch (Throwable e) {
            throw new FilterException(this, e.getMessage());
        }
    }

    // custom serializer for converting spaces to underscores in fiel names
    private static class XmlSpaceToUnderscoreSerializer extends StdSerializer<JsonNode> {
        public XmlSpaceToUnderscoreSerializer() {
            super(JsonNode.class);
        }

        @Override
        public void serialize(JsonNode value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (value.isObject()) {
                gen.writeStartObject();
                for (Iterator<String> it = value.fieldNames(); it.hasNext(); ) {
                    String fieldName = it.next();
                    JsonNode fieldValue = value.get(fieldName);
                    gen.writeFieldName(fieldName.replaceAll(" ", "_"));
                    serialize(fieldValue, gen, provider);
                }
                gen.writeEndObject();
            }
            else if (value.isArray()) {
                gen.writeStartArray();
                for (JsonNode element : value) {
                    serialize(element, gen, provider);
                }
                gen.writeEndArray();
            } else {
                gen.writeString(value.asText());
            }
        }
    }
}
