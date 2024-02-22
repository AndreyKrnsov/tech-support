package techSupport.dto;

public class User {

    private int userId;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private Role role; // Роль пользователя (CLIENT, MANAGER,  PERFORMER, SUPPORT_STAFF, ADMIN)
    private Profile profile; // Профиль исполнителя (PROGRAMMER, MANAGER, DATABASE_SPECIALIST, SYSTEM_ADMINISTRATOR)
    private String info;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String email) {
        this.login = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}