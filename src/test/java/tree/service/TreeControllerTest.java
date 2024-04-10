package tree.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tree.controller.TreeController;
import tree.dataAccess.TreeRepository;
import tree.exception.NodeNotFoundException;
import tree.model.Node;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
@WebMvcTest(TreeController.class)
class TreeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TreeRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getTree() throws Exception {
        Node rootNode = new Node("root", null, "Root Node");
        when(repository.getTree()).thenReturn(Optional.of(rootNode));

        mvc.perform(MockMvcRequestBuilders.get("/api/tree/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(rootNode.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].label").value(rootNode.getLabel()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].children").isEmpty());
    }

    @Test
    void append() throws Exception {
        Node rootNode = new Node("node_id", null, "root label");
        when(repository.addNode(null, "root label")).thenReturn(rootNode.getId());

        mvc.perform(MockMvcRequestBuilders.post("/api/tree/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"parent\": null, \"label\": \"root label\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(equalTo("\""+rootNode.getId()+"\"")));
    }

    @Test
    void appendToNonExistent() throws Exception {
        when(repository.addNode("nonexistent", "lbl")).thenThrow(NodeNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.post("/api/tree/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"parent\": \"nonexistent\", \"label\": \"lbl\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("parent ID nonexistent not found."));
    }
}