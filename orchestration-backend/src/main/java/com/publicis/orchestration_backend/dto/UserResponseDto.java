package com.publicis.orchestration_backend.dto;

import com.publicis.orchestration_backend.entity.User;

import java.util.List;

public class UserResponseDto {
    private List<User> users;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
