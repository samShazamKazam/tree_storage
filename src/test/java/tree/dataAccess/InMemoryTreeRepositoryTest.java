package tree.dataAccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTreeRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    private InMemoryTreeRepository treeRepository;

    @BeforeEach
    void setup() {
        treeRepository = new InMemoryTreeRepository();
    }

    @Test
    void testGetTreeWhenEmpty() {
        Optional<Node> result = treeRepository.getTree();
        assertFalse(result.isPresent());
    }

    @Test
    void testGetTree() throws NodeNotFoundException {
        // Set up test data
        String rootNodeId =  treeRepository.addNode(null, "Root Node");

        Optional<Node> result = treeRepository.getTree();

        assertTrue(result.isPresent());
        assertEquals(rootNodeId, result.get().getId());
    }

    @Test
    void testAddNodeWhenRoot() throws NodeNotFoundException {
        String addedNodeID = treeRepository.addNode(null, "Root Node");

        assertNotNull(addedNodeID);
    }

    @Test
    void testAddNode() throws NodeNotFoundException {
        // Set up test data
        String parentNodeId = treeRepository.addNode(null, "Parent Node");

        String addedNode = treeRepository.addNode(parentNodeId, "Child Node");

        assertNotNull(addedNode);
    }

    @Test
    void testAddNodeWithInvalidParent() {
        assertThrows(NodeNotFoundException.class, () -> {
            treeRepository.addNode("nonexistent", "Child Node");
        });
    }
}