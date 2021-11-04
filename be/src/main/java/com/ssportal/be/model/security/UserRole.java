package com.ssportal.be.model.security;



import com.ssportal.be.model.User;


import java.io.Serializable;


public class UserRole implements Serializable {
    private static final long serialVersionUID = 890345L;

    private long userRoleId;

    public UserRole () {}

    public UserRole (User user, Role role) {
        this.user = user;
        this.role = role;
    }

    private User user;

    private Role role;

    public long getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(long userRoleId) {
        this.userRoleId = userRoleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


}
