package tree.converter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tree.model.Node;

import java.io.IOException;

/**
 * serializes a tree to a JSON string. used by controllers to serialize into JSON for clients
 */
public class NodeSerializer extends JsonSerializer<Node> {

    @Override
    public void serialize(Node node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectFieldStart(String.valueOf(node.getId()));
        jsonGenerator.writeStringField("label", node.getLabel());
        if (!node.getChildren().isEmpty()) {
            jsonGenerator.writeArrayFieldStart("children");
            for (Node child : node.getChildren()) {
                serialize(child, jsonGenerator, serializerProvider);
            }
            jsonGenerator.writeEndArray();
        } else {
            jsonGenerator.writeArrayFieldStart("children");
            jsonGenerator.writeEndArray();
        }
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}