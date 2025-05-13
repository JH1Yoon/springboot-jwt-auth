package com.develop.springguard.domain.auth.entity;

import com.develop.springguard.domain.auth.enums.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private Set<UserRoleEnum> roles;

    public User(String username, String password, String nickname, Set<UserRoleEnum> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public User(Long id, String username, String password, String nickname, Set<UserRoleEnum> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }
}