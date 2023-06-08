package net.canopy.filters.builtin.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ValueNode;

import net.canopy.app.api.CanopyException;

/**
 * The ValueTransformer class is a utility class for transforming the values of a given JSON structure.
 */
public abstract class ValueTransformer {

    /**
     * Recursively traverses the given JSON tree and constructs a new tree with transformed values.
     * The transformation is done by the transformValue function.
     *
     * @param jsonNode The JSON tree to transform.
     * @return The transformed JSON tree.
     * @throws CanopyException If an error occurs while transforming the JSON data. This exception should never occur.
     */
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

    /**
     * Transforms the given value node.
     * The transformation is done by the transformValue function.
     *
     * @param value The value node to transform.
     * @return The transformed value node.
     * @throws CanopyException If an error occurs while transforming the JSON data. This exception should never occur.
     */
    protected abstract ValueNode transformValue(ValueNode value) throws CanopyException;

}
