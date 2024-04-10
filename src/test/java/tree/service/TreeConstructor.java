package tree.service;

import tree.model.Node;

/**
 * Temporary class to provide dummy data for unit testing and manual testing
 */
public class TreeConstructor {
    public static Node constructTree() {
        Node root = new Node("1", null, "root");

        Node ant = new Node("2", root, "ant");
        Node bear = new Node("3", root, "bear");
        Node cat = new Node("4", bear, "cat");
        Node dog = new Node("5", bear, "dog");
        Node elephant = new Node("6", dog, "elephant");
        Node frog = new Node("7", root, "frog");

        root.addChild(ant, bear, frog);
        dog.addChild(elephant);
        bear.addChild(cat, dog);

        return root;
    }
}
