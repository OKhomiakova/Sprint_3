package POJO;

public class CourierForSignUp {
    private String login;
    private String password;
    private String firstName;

    // конструктор со всеми параметрами
    public CourierForSignUp(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    // конструктор без параметров
    public CourierForSignUp() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
