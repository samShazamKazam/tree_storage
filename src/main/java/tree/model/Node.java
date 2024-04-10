package tree.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Node {
    @Id
    private String id;
    private String label;

    private String parent_id;

    private int level;

    @Transient
    private Node parent;
    @Transient
    private List<Node> children = new ArrayList<>();

    public Node() {}

    public Node(String id, Node parent, String label) {
        this.id = id;
        this.label = label;
        this.parent = parent;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void addChild(Node ...nodes) {
        if (nodes == null) return;

        for (Node n : nodes) {
            this.children.add(n);
        }
    }
    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getParent_id() {
        return parent_id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", level=" + level +
                '}';
    }
}