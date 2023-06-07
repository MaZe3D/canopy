package net.canopy.filters;

import net.canopy.app.IFilter;

import com.fasterxml.jackson.core.format.InputAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class Encrypt implements IFilter {
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        System.out.println("Encrypting...");
        ObjectMapper mapper = new ObjectMapper();
        return encryptTree(jsonNode, parameter);
    }

    //recursively traverse Json tree while creating separate encrypted Json tree
    private JsonNode encryptTree(JsonNode jsonNode, String parameter){
        ObjectMapper mapper = new ObjectMapper();
        if(jsonNode.isObject()){
            //create Object JsonNode
            ObjectNode objectNode = mapper.createObjectNode();
            //iterate over the fields in the object node 
            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = jsonNode.fields();
            fieldsIterator.forEachRemaining(entry -> {
                //set fields with fieldnames
                objectNode.set(entry.getKey(), encryptTree(entry.getValue(), parameter));
            });
            return objectNode;
        }
        else if(jsonNode.isArray()){
            //create Array JsonNode
            ArrayNode arrayNode = mapper.createArrayNode();
            //iterate over all elements in the arraynode 
            Iterator<JsonNode> elements = jsonNode.elements();
            elements.forEachRemaining(element -> {
                //add elements to array
                arrayNode.add(encryptTree(element, parameter));
            });
            return arrayNode;
        }
        else if(jsonNode.isValueNode()){
            return new TextNode(encrypt(jsonNode.asText(), parameter)); 
        }
        return null;
    }
    private String encrypt(String inputString, String keyString){
        // encrypt by performing xor with the input and key chars 
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < inputString.length(); i++){
            output.append((char) (inputString.charAt(i)^keyString.charAt(i % keyString.length())));
        }
        return output.toString();
    }
}

