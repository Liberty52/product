package com.liberty.authentication.core.token;

import com.liberty.authentication.core.UserRole;
import com.liberty.authentication.core.principal.User;

import java.util.Collection;

public class UserAuthenticationToken extends AbstractAuthenticationToken {

    private final User principal;

    public UserAuthenticationToken(User principal, Collection<UserRole> authorities) {
        super(authorities);
        this.principal = principal;
        setAuthentication(true);
    }

    @Override
    public User getPrincipal() {
        return this.principal;
    }

    @Override
    public String toString() {
        return "UserAuthenticationToken{" +
                "principal=" + principal +
                '}';
    }
}
