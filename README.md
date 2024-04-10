# Introduction

## Problem
How to store trees? How can we store it in DB? How does that affect our queries? How to fetch a subtree?
or delete a node?

## Solution
The choice of the best database for storing and searching trees depends on various factors, including the specific
requirements of your application, the scale of your data, and the type of queries you need to perform. Let's explore
some options:

1. **SQL Databases**:
- **Adjacency List Model**: In SQL databases, you can represent trees using the adjacency list model, where each node contains a reference to its parent node. This approach is straightforward to implement and efficient for storing hierarchical data. However, querying and traversing trees stored in this model can be complex and may require recursive queries, which might not perform well on large datasets.
- **Nested Set Model**: Another option in SQL databases is the nested set model, where each node in the tree is assigned a left and right value representing its position within the tree hierarchy. This model allows for efficient querying of subtrees and determining ancestor-descendant relationships. However, maintaining the nested set structure can be complex and may require additional overhead during insertions and deletions.

2. **NoSQL Databases**:
- **Document Databases**: NoSQL document databases like MongoDB or Couchbase can be suitable for storing trees, especially if your tree data has variable or nested structures. You can store each tree as a document, with nested objects representing nodes and their relationships. Document databases offer flexibility in schema design and support querying based on nested attributes, making them suitable for storing and searching trees.
- **Key-Value Stores**: Some NoSQL key-value stores like Redis can also be used for storing trees by representing each node as a key-value pair, with keys representing node identifiers and values containing node attributes or references. While key-value stores are efficient for fast key-based lookups, querying and traversing the tree structure might require additional application logic.

3. **Graph Databases**:
- **Graph databases like Neo4j**: Graph databases are specifically designed for storing and querying graph-structured data, making them an excellent choice for storing trees and performing complex graph-based queries. Nodes and edges in the tree can be represented as entities and relationships in the graph database, allowing for efficient traversal and querying of tree structures. Graph databases provide built-in support for graph algorithms and queries, making it easier to search for values and perform graph-based operations on trees.

Ultimately, the best option for storing and searching trees depends on your specific use case, performance requirements,
scalability needs, and familiarity with the database technology. It's essential to evaluate the trade-offs and
characteristics of each database type before making a decision.

# SQL DB Method Overview 

The tree is broken down into nodes and each node is a row and has a pointer (a foreign key) 
pointing to its parent.

## Data Schema

The fields are:
- id: the ID of the node
- label: the label of the node
- parent_id: the ID of a parent node or null for the root node
- level: the depth of the node in the tree.

```shell
  Column   |          Type          | Collation | Nullable | 
-----------+------------------------+-----------+----------+
 id        | character varying(255) |           | not null |
 label     | character varying(255) |           |          |
 parent_id | character varying(255) |           |          |
 level     | integer                |           | not null |
 
Indexes:
    "node_pkey" PRIMARY KEY, btree (id)
Check constraints:
    "node_level_check" CHECK (level >= 0)
Foreign-key constraints:
    "fkni535kidjh6jci6wtjcdw07la" FOREIGN KEY (parent_id) REFERENCES node(id)
    
Referenced by:
    TABLE "node" CONSTRAINT "fkni535kidjh6jci6wtjcdw07la" FOREIGN KEY (parent_id) REFERENCES node(id)
```


### Queries

#### Getting a Subtree
* Getting all the nodes: we can simply ask the DB server to return all the nodes.
That would make building the tree easier as the nodes of one level are the parents of the next level. The root
will be the first record with level 0. 
  ```
  SELECT * FROM node ORDER BY level ASC;
  ```

#### Adding a Node
* Adding a node to a parent node

  Inserting requires one statement with a valid parent id or null to insert the root node with level 0.
  ```
  INSERT INTO node (id, label, parent_id, level)
  SELECT 'node_id_value', 'node_label_value', n.id, n.level + 1
  FROM node n
  WHERE n.id = 'some_parent_id';
  ```
  

For a tree like this,
```bash
   z
  / \
 x   y
    / 
   w
   ````
we can represent it as 
```
[
  {
    "00e9d08c-ca72-4604-96da-90caa6f3519b": {
      "label": "z",
      "children": [
        {
          "48aa69ac-2c0e-49ad-8486-2c014608a6fa": {
            "label": "x",
            "children": []
          }
        },
        {
          "49769f5e-485c-4d01-8721-f9ad01d1a279": {
            "label": "y",
            "children": [
              {
                "99d4d990-5b6f-4b54-9896-6d5bbf1b68fc": {
                  "label": "w",
                  "children": []
                }
              }
            ]
          }
        }
      ]
    }
  }
]
```

# NoSQL DB Method Overview

I chose MongoDB here for no particular reason but familiarity. For handling hierarchical data 
within the context of MongoDB the approaches include:

- Modeling tree structures using parent references.
- Modeling tree structures using child references.
- Modeling tree structures using an array of ancestors.

## Data Schema
- Modeling tree structures using parent references.
(ID, ParentReference, Order) where order is integer to order siblings

- Modeling tree structures using child references.
  (ID, ChildReferences)

- Modeling tree structures using an array of ancestors.
  (ID, ParentReference, AncestorReferences)

### Queries 
#### Getting a Subtree
#### Adding a Node

# Graph DB Method Overview
## Data Schema
nodes represent value and their relationship is represented by "child"

```bash
    1
   / \
  2   3
 / \
4   5

```
```markdown
// nodes representing integers
CREATE (:Node {value: 1})
CREATE (:Node {value: 2})
CREATE (:Node {value: 3})
CREATE (:Node {value: 4})
CREATE (:Node {value: 5})

// relationship to represent the tree structure 
MATCH (parent:Node {value: 1})
MATCH (child:Node {value: 2})
CREATE (parent)-[:CHILD]->(child)

MATCH (parent:Node {value: 1})
MATCH (child:Node {value: 3})
CREATE (parent)-[:CHILD]->(child)

MATCH (parent:Node {value: 2})
MATCH (child:Node {value: 4})
CREATE (parent)-[:CHILD]->(child)

MATCH (parent:Node {value: 2})
MATCH (child:Node {value: 5})
CREATE (parent)-[:CHILD]->(child)

```
#### Getting a Subtree
To get the subtree rooted at node 2 for example and its children

```markdown
MATCH p = (n:Node {value: 2})-[:CHILD*]->(child)
RETURN p
```

#### Adding a Node


## References
- https://www.mongodb.com/docs/manual/applications/data-models-tree-structures/
- https://www.codeproject.com/Articles/521713/Storing-Tree-like-Hierarchy-Structures-With-MongoD
- https://www.codeproject.com/Articles/522746/Storing-Tree-like-Hierarchy-Structures-With-Mong-2