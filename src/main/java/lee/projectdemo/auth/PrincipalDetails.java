package lee.projectdemo.auth;

import lee.projectdemo.login.user.User;
import lee.projectdemo.login.user.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private final User user;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getId();
    }

    //권한을 조회해서 가져오는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        UserRole userRole = user.getUserRole();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        collect.add(authority);
        return collect;
    }
    

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

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
        // 휴면 계정 등등 관리
    }
}
