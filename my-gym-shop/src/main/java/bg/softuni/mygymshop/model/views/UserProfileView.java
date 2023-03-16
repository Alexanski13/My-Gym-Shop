package bg.softuni.mygymshop.model.views;

public class UserProfileView {
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private int age;

    public UserProfileView() {
    }

    public UserProfileView(String username, String email, String firstName, String lastName, int age) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    public UserProfileView setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileView setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserProfileView setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserProfileView setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserProfileView setAge(int age) {
        this.age = age;
        return this;
    }
}
