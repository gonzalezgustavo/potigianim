package com.example.potigianim.model;

public class User {
    private String code;
    private String name;
    private String accessCode;

    public String getCode() {
        return code.trim();
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name.trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessCode() {
        return accessCode.trim();
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
