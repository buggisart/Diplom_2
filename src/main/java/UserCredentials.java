public class UserCredentials {

    private String email;
    private String password;

    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public UserCredentials(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword(), user.getName());
    }

}

