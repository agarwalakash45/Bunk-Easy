package akashagarwal45.bunkeasy;

public class Users {
    private String name;
    private String userName;
    private String passWord;
    private String email;

    public Users(){

    }

    //Setters Method
    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Getters method
    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getEmail() {
        return email;
    }
}