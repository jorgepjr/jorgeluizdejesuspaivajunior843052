package com.musiccatalog.model.account;

public enum UserRole {
        USER("admin"),
        ADMIN("user");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole() {return role;}
}
