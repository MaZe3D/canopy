package net.canopy.filters;

import net.canopy.app.IFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

public class Decrypt extends ValueTransformer implements IFilter {

    private AesCryptor aesCryptor;

    @Override
    public JsonNode apply(JsonNode jsonNode, String parameter) {
        System.out.println("Decrypting...");
        this.aesCryptor = new AesCryptor(parameter); // use parameter as AES password
        return this.transformTree(jsonNode);
    }

    protected ValueNode transformValue(ValueNode value) {
        return new TextNode(this.aesCryptor.decrypt(value.asText()));
    }
}
