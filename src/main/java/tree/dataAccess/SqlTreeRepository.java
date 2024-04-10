package tree.dataAccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.*;

@Repository
public class SqlTreeRepository implements TreeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Node> getTree() {
        List<Node> allNodes = entityManager.createQuery("SELECT n FROM Node n ORDER BY level ASC", Node.class).getResultList();
        System.out.format("number of nodes = %d\n", allNodes.size());
        if (allNodes.isEmpty()) {
            return Optional.empty();
        }
        Map<String, Node> map = new HashMap<>();
        allNodes.stream().forEach(node -> map.put(node.getId(), node));

        Node root = null;
        for (Node node : allNodes) {
//            System.out.format("node: %s\n", node.toString());
            if (node.getParent_id() == null) root = node;
            else map.get(node.getParent_id()).addChild(node);
        }

        return Optional.of(root);
    }

    @Transactional
    @Override
    public String addNode(String parent_id, String label) throws NodeNotFoundException {
        String uuid = UUID.randomUUID().toString();
        if (parent_id == null) {
            int updatedRows = entityManager.createQuery(
                            "INSERT INTO Node u (id, label, parent_id, level)\n" +
                                    "  VALUES (?1, ?2, null, 1)")
                    .setParameter(1, uuid)
                    .setParameter(2, label)
                    .executeUpdate();
        } else {
            int updatedRows = entityManager.createQuery(
                            "INSERT INTO Node u (id, label, parent_id, level)\n" +
                                    "  SELECT ?1, ?2, n.id, n.level + 1\n" +
                                    "  FROM Node n\n" +
                                    "  WHERE n.id = ?3")
                    .setParameter(1, uuid)
                    .setParameter(2, label)
                    .setParameter(3, parent_id)
                    .executeUpdate();
        }
        return uuid;
    }
}
