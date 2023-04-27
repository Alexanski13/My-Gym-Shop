package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.comment.CommentDTO;
import bg.softuni.mygymshop.model.entities.CommentEntity;
import bg.softuni.mygymshop.model.entities.ProductEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CommentService commentService;

    @Captor
    private ArgumentCaptor<CommentEntity> commentCaptor;

    @Test
    public void createComment_validData_commentIsSaved() {
        CommentDTO commentDto = new CommentDTO();
        commentDto.setText("Test comment!");
        long productId = 1;
        UserEntity author = new UserEntity();
        ProductEntity product = new ProductEntity();
        when(productService.getProductById(productId)).thenReturn(product);

        commentService.createComment(commentDto, productId, author);

        verify(commentRepository, times(1)).save(commentCaptor.capture());

        CommentEntity argument = commentCaptor.getValue();
        assertEquals("Test comment!", argument.getText());
        assertEquals(author, argument.getAuthor());
        assertEquals(product, argument.getProduct());
    }
}
