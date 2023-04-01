package bg.softuni.mygymshop.model.views;

public class CommentView {

    private Long id;
    private String text;
    private String authorName;
    private String dateOfCreation;

    private boolean canEdit;

    public CommentView(Long id, String text, String authorName, String dateOfCreation, boolean canEdit) {
        this.id = id;
        this.text = text;
        this.authorName = authorName;
        this.dateOfCreation = dateOfCreation;
        this.canEdit = canEdit;
    }

    public Long getId() {
        return id;
    }

    public CommentView setId(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentView setText(String text) {
        this.text = text;
        return this;
    }

    public String getAuthorName() {
        return authorName;
    }

    public CommentView setAuthorName(String authorName) {
        this.authorName = authorName;
        return this;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public CommentView setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
        return this;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public CommentView setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
        return this;
    }
}
