package tree.dataAccess;

import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.Optional;

public interface TreeRepository {

    /**
     * @return the whole tree
     */
    Optional<Node> getTree();

    /**
     * adds a new child node under a node with parent_id
     *
     * @param parent_id the ID for the parent node
     * @param label     the label for the new node
     * @return the newly created node appended under the node with parent_id
     * @throws NodeNotFoundException if the parent_id is invalid
     */
    String addNode(String parent_id, String label) throws NodeNotFoundException;
}
