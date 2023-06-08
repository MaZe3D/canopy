package net.canopy.filters.builtin;

import net.canopy.app.api.IFilter;
import net.canopy.app.api.Logger;

import net.canopy.filters.builtin.util.AesCryptor;
import net.canopy.filters.builtin.util.ValueTransformer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

import net.canopy.app.api.CanopyException;
import net.canopy.app.api.FilterException;

/**
 * The Decrypt class is a built-in filter that decrypts the values of a given JSON structure.
 * It implements the IFilter interface.
 * It uses the AES algorithm to decrypt the values.
 * The password for the decryption is specified as the parameter of the filter.
 */

public class Decrypt extends ValueTransformer implements IFilter {

    private Logger logger = new Logger(this.getClass().getName());

    private AesCryptor aesCryptor;

    /**
     * Applies the decryption filter to the given JSON node.
     *
     * @param jsonNode   The JSON node to apply the filter on.
     * @param parameter  The password to use for the decryption.
     * @return The decrypted JSON node.
     * @throws FilterException If an error occurs while decrypting the JSON data.
     */
    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) throws FilterException {
        logger.log("Decrypting with password: " + parameter);
        try {
            this.aesCryptor = new AesCryptor(parameter); // use parameter as AES password
            return this.transformTree(jsonNode);
        } catch (CanopyException e) {
            throw new FilterException(this, e.getMessage());
        }
    }

    protected ValueNode transformValue(ValueNode value) throws CanopyException {
        return new TextNode(this.aesCryptor.decrypt(value.asText()));
    }
}
