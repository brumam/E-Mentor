package com.example.e_mentor.Helpers;

public class User {
    private String email, password, firstName, lastName;
    private String imageUrl;
    private String userType;

    public User() {

    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(String email, String password, String firstName, String lastName, String imageUrl, String userType) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.userType = userType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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
    public boolean isStudent() {
        return "student".equalsIgnoreCase(userType);
    }
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(userType);
    }
    public boolean isTeacher() {
        return "teacher".equalsIgnoreCase(userType);
    }

}
