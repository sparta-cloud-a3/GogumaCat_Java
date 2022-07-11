package com.example.goguma.security;

import com.example.goguma.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public User getUser() {
        return user;
    }

    //닉네임 가져오기 추가. UserDetails 자체에 getNickname()이 없어서 Override가 아닌 그냥 작성인 듯!
    public Long getId() { return  user.getId();}
    public String getNickname() { return user.getNickname(); }
    public String getProfilePic() { return user.getProfilePic(); }
    public String getProfileInfo() { return user.getProfileInfo(); }
    public String getAddress() { return user.getAddress(); }
    public Long getKakaoId() { return user.getKakaoId();}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}

