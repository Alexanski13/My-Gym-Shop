package bg.softuni.mygymshop.model.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private LocalDateTime posted;

    public FeedbackEntity() {
    }

    public Long getId() {
        return id;
    }

    public FeedbackEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public FeedbackEntity setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public LocalDateTime getPosted() {
        return posted;
    }

    public FeedbackEntity setPosted(LocalDateTime posted) {
        this.posted = posted;
        return this;
    }
}
