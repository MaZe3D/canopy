package net.canopy.filters;


import net.canopy.app.IFilter;
import net.canopy.app.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Extract implements IFilter {
    private Logger logger = new Logger(this.getClass().getName());

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        logger.log("Extracting " + parameter + "...");
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



