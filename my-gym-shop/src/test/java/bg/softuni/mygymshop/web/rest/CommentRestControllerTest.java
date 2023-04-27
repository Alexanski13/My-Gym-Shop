package bg.softuni.mygymshop.web.rest;

import bg.softuni.mygymshop.model.dtos.comment.CommentDTO;
import bg.softuni.mygymshop.model.entities.CommentEntity;
import bg.softuni.mygymshop.model.entities.RoleEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.enums.RoleType;
import bg.softuni.mygymshop.service.CommentService;
import bg.softuni.mygymshop.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CommentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void getAllComments_requestIsMade_allCommentsReturned() throws Exception {
        when(commentService.getCommentsByProduct(1L)).thenReturn(List.of(
                createComment("Test comment 1"), createComment("Test comment 2")
        ));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/1/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].text", is("Test comment 1")))
                .andExpect(jsonPath("$.[0].authorName", is("Testovnull")))
                .andExpect(jsonPath("$.[1].text", is("Test comment 2")))
                .andExpect(jsonPath("$.[1].authorName", is("Testovnull")));
    }

    @Test
    public void getComment_commentExists_commentReturned() throws Exception {
        when(commentService.getComment(1L)).thenReturn(createComment("Test comment."));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is("Test comment.")))
                .andExpect(jsonPath("$.authorName", is("Testovnull")))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getComment_commentDoesNotExist_notFoundStatusIsReturned() throws Exception {
        when(commentService.getComment(1L)).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/1/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createComment_anonymousUser_forbiddenStatusIsReturned() throws Exception {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setText("Test comment!");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/1/comments")
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    public void deleteComment_anonymousUser_forbiddenStatusIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/1/comments/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "adminUser")
    public void deleteComment_withAdmin_commentIsDeletedSuccessfully() throws Exception {
        UserEntity admin = new UserEntity();
        admin.setUsername("adminUser");
        RoleEntity role = new RoleEntity();
        role.setRole(RoleType.ADMIN);
        admin.setRoles(Collections.singleton(role));
        when(userService.getUserByUsername("adminUser")).thenReturn(admin);
        CommentEntity comment = createComment("Comment to be deleted!");
        when(commentService.getComment(1L)).thenReturn(comment);
        when(commentService.deleteComment(1L)).thenThrow(new RuntimeException("Comment not found"));

        mockMvc.perform(delete("/api/1/comments/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "adminUser")
    public void deleteComment_withAdminCommentDoesNotExist_notFoundStatusIsReturned() throws Exception {
        UserEntity admin = new UserEntity();
        admin.setUsername("adminUser");
        RoleEntity role = new RoleEntity();
        role.setRole(RoleType.ADMIN);
        admin.setRoles(Collections.singleton(role));
        when(userService.getUserByUsername("adminUser")).thenReturn(admin);
        when(commentService.getComment(1L)).thenThrow(RuntimeException.class);

        mockMvc.perform(delete("/api/1/comments/1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    private CommentEntity createComment(String text) {
        UserEntity author = new UserEntity();
        author.setId(1L);
        author.setUsername("testUser");
        author.setFirstName("Testov");

        CommentEntity comment = new CommentEntity();
        comment.setCreated(LocalDateTime.now());
        comment.setText(text);
        comment.setAuthor(author);
        comment.setId(1L);

        return comment;
    }
}