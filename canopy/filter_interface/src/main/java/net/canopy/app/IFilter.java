package net.canopy.app;

import com.fasterxml.jackson.databind.JsonNode;

public interface IFilter {
    default JsonNode apply(JsonNode jsonNode, String parameter) {
        System.out.println("standard filter implementation of " + this.getClass());
        return jsonNode;
    }
}
