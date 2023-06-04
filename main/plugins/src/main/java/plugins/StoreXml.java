package main;

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

public class StoreXml implements IFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        try {
            ObjectNode root = new ObjectNode(JsonNodeFactory.instance);
            root.set("row", jsonNode);

            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // create a custom serializer to replace spaces with underscores in field names
            SimpleModule module = new SimpleModule();
            module.addSerializer(JsonNode.class, new CustomJsonNodeSerializer());
            xmlMapper.registerModule(module);

            // Convert JsonNode to XML
            String xml = xmlMapper.writeValueAsString(root);

            // Print the XML output
            System.out.println(xml);

            return jsonNode;

        }
        catch (Throwable e) {
            System.out.println(e);
            return null;
        }
    }

    private static class CustomJsonNodeSerializer extends StdSerializer<JsonNode> {
        public CustomJsonNodeSerializer() {
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
