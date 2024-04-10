package tree.dto;

/**
 * object representing requests to append a node to a tree
 */
public class AppendRequest {
    private String parent;
    private String label;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
