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

public class Decrypt extends ValueTransformer implements IFilter {

    private Logger logger = new Logger(this.getClass().getName());

    private AesCryptor aesCryptor;

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
