package POJO;

public class CourierForSignIn {
    private String login;
    private String password;

    // конструктор со всеми параметрами
    public CourierForSignIn(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // конструктор без параметров
    public CourierForSignIn() {
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
}


