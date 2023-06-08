package net.canopy.filters.builtin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ValueNode;

import net.canopy.app.api.CanopyException;

// class for iterating over a json tree for the purpose transforming all value nodes
public abstract class ValueTransformer {

    // recursively traverse json tree to construct new tree with transformed values
    protected JsonNode transformTree(JsonNode jsonNode) throws CanopyException {

        // ValueNode -> text representation -> TextNode with transformed text
        if (jsonNode.isValueNode()) {
            return this.transformValue((ValueNode)jsonNode);
        }

        // ObjectNode -> ObjectNode with same fields but transformed values
        if (jsonNode.isObject()) {
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            
            for (var it = jsonNode.fields(); it.hasNext(); ) {
                var field = it.next();
                objectNode.set(field.getKey(), this.transformTree(field.getValue()));
            }


            return objectNode;
        }

        // ArrayNode -> ArrayNode with transformed elements
        if (jsonNode.isArray()) {
            ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode();

            for (var it = jsonNode.elements(); it.hasNext(); ) {
                var element = it.next();
                arrayNode.add(this.transformTree(element));
            }

            return arrayNode;
        }

        return null;
    }

    // function that does the actual transformation of the values - may not modify value!
    protected abstract ValueNode transformValue(ValueNode value) throws CanopyException;

}
