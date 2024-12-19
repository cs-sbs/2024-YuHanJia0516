package org.example;

public abstract class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public User(){
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public abstract void book_add_update();
    public abstract void book_delete();
    public abstract void book_search();

}
