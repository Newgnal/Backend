package com.tave.alarmissue.auth.dto.request;

import com.tave.alarmissue.user.domain.UserEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class PrincipalUserDetails implements UserDetails, OAuth2User {

    private final UserEntity userEntity;
    private final Map<String, Object> attributes;

    public PrincipalUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
        this.attributes = new HashMap<>();
    }

    // 일반 user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleValue = userEntity.getRole().getValue();
        return List.of(new SimpleGrantedAuthority(roleValue));
    }

    public Long getUserId() {
        return userEntity.getId();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return userEntity.getId().toString();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userEntity.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userEntity.isEnabled();
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }


    // OAuth2User
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return userEntity.getEmail();
    }
}
