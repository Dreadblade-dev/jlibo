package com.dreadblade.jlibo.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN("Administrator"),
    USER("User");

    @Override
    public String getAuthority() {
        return name();
    }

    Role(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
