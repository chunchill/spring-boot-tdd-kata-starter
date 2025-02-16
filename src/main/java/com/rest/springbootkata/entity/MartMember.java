package com.rest.springbootkata.entity;

public class MartMember {
    private Integer id;
    private String memberType;
    private boolean isBirthday;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public boolean isBirthday() {
        return isBirthday;
    }

    public void setBirthday(boolean birthday) {
        isBirthday = birthday;
    }
}