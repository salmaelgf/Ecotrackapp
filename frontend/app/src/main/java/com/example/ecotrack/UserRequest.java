package com.example.ecotrack;

public class UserRequest {
    private String email;
    private String password;
    private String sex;
    private String birthDate;


    public UserRequest(String email, String password) {
        this.email = email;
        this.password = password;}


    public UserRequest(String email, String password, String sex, String birthDate) {
        this.email = email;
        this.password = password;
        this.sex = sex;
        this.birthDate = birthDate;
    }

}
