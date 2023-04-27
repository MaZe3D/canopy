package main;

import com.fasterxml.jackson.databind.JsonNode;

public interface IFilter {
    static JsonNode apply(JsonNode jsonNode, String parameter) {
        System.out.println(parameter);
        return jsonNode;
    }
}
