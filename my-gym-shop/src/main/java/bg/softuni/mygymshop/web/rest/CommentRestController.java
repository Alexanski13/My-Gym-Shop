package bg.softuni.mygymshop.web.rest;

import bg.softuni.mygymshop.model.dtos.comment.CommentDTO;
import bg.softuni.mygymshop.model.entities.CommentEntity;
import bg.softuni.mygymshop.model.entities.UserEntity;
import bg.softuni.mygymshop.model.views.CommentView;
import bg.softuni.mygymshop.service.CommentService;
import bg.softuni.mygymshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static bg.softuni.mygymshop.model.enums.RoleType.ADMIN;

@RestController
public class CommentRestController {

    private final CommentService commentService;

    private final UserService userService;

    @Autowired
    public CommentRestController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/api/{productId}/comments")
    public ResponseEntity<List<CommentView>> getCommentsProducts(@PathVariable("productId") Long productId, Principal principal) {
        UserEntity user = null;
        try {
            user = userService.getUserByUsername(principal.getName());
        } catch (RuntimeException e) {
            //IGNORE
        }
        var comments = commentService.getCommentsByProduct(productId)
                .stream().map(createCommentViewForUser(principal, user))
                .collect(Collectors.toList());

        return ResponseEntity.ok(comments);
    }

    private Function<CommentEntity, CommentView> createCommentViewForUser(Principal principal, UserEntity user) {
        return c -> {
            boolean canEdit = principal != null &&
                    (isAdmin(user) || user.getId().equals(c.getAuthor().getId()));
            return mapToCommentView(c, canEdit);
        };
    }

    private CommentView mapToCommentView(CommentEntity c) {
        return mapToCommentView(c, false);
    }

    private CommentView mapToCommentView(CommentEntity c, boolean canEdit) {
        return new CommentView(c.getId(), c.getText(), c.getAuthor().getFirstName() + c.getAuthor().getLastName(),
                c.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")), canEdit);
    }



    @GetMapping("/api/{productId}/comments/{commentId}")
    private ResponseEntity<CommentView> getComment(@PathVariable("commentId") Long commentId) {
        try {
            return ResponseEntity.ok(mapToCommentView(commentService.getComment(commentId)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/api/{productId}/comments", consumes = "application/json", produces = "application/json")
    public ResponseEntity<CommentView> createComment(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody CommentDTO commentDTO,
                                                     @PathVariable("productId") Long productId) {
        CommentEntity comment = commentService.createComment(commentDTO,
                productId, userService.getUserByUsername(userDetails.getUsername()));

        CommentView commentView = mapToCommentView(comment);

        return ResponseEntity.created(URI.create(String.format("/api/%s/comments/%d", productId, comment.getId())))
                .body(commentView);
    }



    @DeleteMapping("/api/{productId}/comments/{commentId}")
    public ResponseEntity<CommentView> deleteComment(@PathVariable("commentId") Long commentId,
                                                     @AuthenticationPrincipal UserDetails principal) {
        UserEntity user = userService.getUserByUsername(principal.getUsername());
        try {
            return deleteCommentInternal(commentId, user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<CommentView> deleteCommentInternal(Long commentId, UserEntity user) {
        CommentEntity comment = commentService.getComment(commentId);

        if(isAdmin(user) || user.getId().equals(comment.getAuthor().getId())) {
            CommentEntity deleted = commentService.deleteComment(commentId);
            return ResponseEntity.ok(mapToCommentView(deleted));
        }
        return ResponseEntity.status(403).build();
    }

    private boolean isAdmin(UserEntity user) {
        return user.getRoles().stream().anyMatch(r -> r.getRole() == ADMIN);
    }
}
