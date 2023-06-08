
package net.canopy.filters.builtin;

import net.canopy.app.api.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * The Extract class is a built-in filter that extracts specific JSON nodes from a given JSON structure.
 * It implements the IFilter interface.
 */
public class Extract implements IFilter {
    private Logger logger = new Logger(this.getClass().getName());

/**
 * Applies the extraction filter to the given JSON node.
 *
 * @param jsonNode   The JSON node to apply the filter on.
 * @param parameter  The name of the JSON node to extract.
 * @return The extracted JSON nodes as an ArrayNode.
 */
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        logger.log("Extracting " + parameter);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode resultArray = mapper.createArrayNode();
        //iterate through all JsonNodes with the parameter as its name
        for (JsonNode foundJsonNode : jsonNode.findValues(parameter)) {
            if(foundJsonNode.isArray()){
                resultArray.addAll((ArrayNode)foundJsonNode);
            }
            else{
                resultArray.add(foundJsonNode);
            }
        }
        return resultArray;
    }
}



