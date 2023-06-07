package net.canopy.filters;

import net.canopy.app.IFilter;

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

public class StoreXml implements IFilter.IStoreFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
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
                System.out.println("No parameter specified, using standard output as target");
                System.out.print(content);
            } else {
                System.out.println("Using parameter as target-path: " + parameter);
                URL url = new URL(new URL("file:"), parameter);
                System.out.println("writing XML to: " + url);
                xmlMapper.writeValue(new File(url.getFile()), nodeToStore);
            }

            return jsonNode;
        }
        catch (Throwable e) {
            System.out.println(e);
            return null;
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
