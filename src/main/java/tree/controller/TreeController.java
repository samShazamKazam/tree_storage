package tree.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tree.dataAccess.TreeRepository;
import tree.dto.AppendRequest;
import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tree/")
public class TreeController {

    @Autowired
    TreeRepository treeRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Node> getTree() {
        Optional<Node> tree = treeRepository.getTree();
        if (tree.isEmpty()) return new ArrayList<>();
        return List.of(tree.get());
    }

    @PostMapping( consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> append(@RequestBody AppendRequest request) {
        try {
            String child = treeRepository.addNode(request.getParent(), request.getLabel());
            return ResponseEntity.ok("\"" + child + "\"");
        } catch (NodeNotFoundException ex) {
            return new ResponseEntity<String>(String.format("parent ID %s not found.", request.getParent())
                    , new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
    }

}