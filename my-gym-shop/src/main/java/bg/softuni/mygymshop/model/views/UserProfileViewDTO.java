package bg.softuni.mygymshop.model.views;

public class UserProfileViewDTO {
    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private int age;

    public String getUsername() {
        return username;
    }

    public UserProfileViewDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserProfileViewDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserProfileViewDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserProfileViewDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public int getAge() {
        return age;
    }

    public UserProfileViewDTO setAge(int age) {
        this.age = age;
        return this;
    }
}
