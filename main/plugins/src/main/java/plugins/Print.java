package main;

import com.fasterxml.jackson.databind.JsonNode;

public class Print implements IFilter {

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        System.out.println(jsonNode.toString());
        return jsonNode;
    }

}
