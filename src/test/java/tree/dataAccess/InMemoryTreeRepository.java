package tree.dataAccess;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;

@Primary
@Repository
public class InMemoryTreeRepository implements TreeRepository {
    // a map to hold the reference to every node by its ID
    private Map<String, Node> map = new TreeMap<>();
    private Node root = null;

    public InMemoryTreeRepository() {
    }

    @Override
    public Optional<Node> getTree() {
        if (root == null) return Optional.empty();

        return Optional.of(map.get(root.getId()));
    }

    private Optional<Node> getTree(String id) {
        return Optional.ofNullable(map.get(id));
    }


    // TODO: the generated ID might be used in the tree already. If so generate again.
    @Override
    public String addNode(String parent_id, String label) throws NodeNotFoundException {
        String generated_id = UUID.randomUUID().toString();

        // if it's the first node and the tree is empty, it is the root node.
        if (root == null && parent_id == null) {
            Node childNode = new Node(generated_id, null, label);
            map.put(childNode.getId(), childNode);
            root = childNode;
            return childNode.getId();
        }

        Optional<Node> parentNodeOptional = getTree(parent_id);
        if (parentNodeOptional.isEmpty()) {
            throw new NodeNotFoundException();
        } else {
            Node parentNode = parentNodeOptional.get();
            Node childNode = new Node(generated_id, parentNode, label);
            parentNode.addChild(childNode);
            map.put(childNode.getId(), childNode);
            return childNode.getId();
        }
    }
}
