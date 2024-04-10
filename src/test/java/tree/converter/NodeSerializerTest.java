package tree.converter;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tree.model.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeSerializerTest {
    private NodeSerializer serializer = new NodeSerializer();
    private Writer jsonWriter;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @BeforeEach
    void setUp() throws IOException {
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void serialize() throws IOException {
        Node node = new Node("1", null, "myLabel");

        serializer.serialize(node, jsonGenerator, serializerProvider);

        jsonGenerator.flush();

        String expectedJson = "{\"1\":{\"label\":\"myLabel\",\"children\":[]}}";
        assertEquals(expectedJson, jsonWriter.toString());
    }

    @Test
    void serializeComplexTree() throws IOException {
        Node root = new Node("1", null, "root");
        Node child1 = new Node("2", root, "child 1");
        Node child2 = new Node("3", root, "child 2");
        root.addChild(child1);
        root.addChild(child2);


        serializer.serialize(root, jsonGenerator, serializerProvider);

        jsonGenerator.flush();

        String expectedJson = "{\"1\":{\"label\":\"root\",\"children\":[" +
                    "{\"2\":{\"label\":\"child 1\",\"children\":[]}}," +
                    "{\"3\":{\"label\":\"child 2\",\"children\":[]}}" +
                "]}}";
        assertEquals(expectedJson, jsonWriter.toString());
    }
}