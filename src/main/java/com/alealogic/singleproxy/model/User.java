package com.alealogic.singleproxy.model;

import java.util.Objects;

public class User {

    private String apiKey;

    public User(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(apiKey, user.apiKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey);
    }
}
