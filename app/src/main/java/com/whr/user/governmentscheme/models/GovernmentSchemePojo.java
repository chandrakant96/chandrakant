package com.whr.user.governmentscheme.models;

public class GovernmentSchemePojo {
    String id;
    String name;

    @Override
    public String toString() {
        return "GovernmentSchemePojo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
