package net.canopy.filters;

import net.canopy.app.IFilter;
import net.canopy.app.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class Encrypt extends ValueTransformer implements IFilter {

    private Logger logger = new Logger(this.getClass().getName());

    private AesCryptor aesCryptor;

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        logger.log("Encrypting...");
        this.aesCryptor = new AesCryptor(parameter); // use parameter as AES password
        return this.transformTree(jsonNode);
    }

    protected ValueNode transformValue(ValueNode value) {
        return new TextNode(this.aesCryptor.encrypt(value.asText()));
    }
}

