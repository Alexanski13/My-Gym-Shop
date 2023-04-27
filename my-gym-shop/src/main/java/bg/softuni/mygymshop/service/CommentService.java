package bg.softuni.mygymshop.service;

import bg.softuni.mygymshop.model.dtos.comment.CommentDTO;
import bg.softuni.mygymshop.model.entities.CommentEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ProductService productService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ProductService productService) {
        this.commentRepository = commentRepository;
        this.productService = productService;
    }

    public List<CommentEntity> getCommentsByProduct(Long productId) {
        return commentRepository.findAllByProduct(productService.getProductById(productId));
    }

    public CommentEntity createComment(CommentDTO commentDTO, Long productId, UserEntity author) {
        CommentEntity comment = new CommentEntity()
                .setCreated(LocalDateTime.now())
                .setProduct(productService.getProductById(productId))
                .setAuthor(author)
                .setText(commentDTO.getText())
                .setApproved(true);

        commentRepository.save(comment);

        return comment;
    }

    public CommentEntity getComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Comment not found!"));
    }

    public CommentEntity deleteComment(Long id) {
        CommentEntity comment = getComment(id);
        commentRepository.delete(comment);
        return comment;
    }
}
