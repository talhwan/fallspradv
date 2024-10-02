package com.thc.fallspradv.security;

import com.thc.fallspradv.domain.Tbuser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class PrincipalDetails implements UserDetails {
	
	private final Tbuser tbuser;
	
	public PrincipalDetails(Tbuser tbuser) {
		this.tbuser = tbuser;
	}
	
	public Tbuser getTbuser() {
        return tbuser;
    }

	@Override
	public String getPassword() {
		return tbuser.getPassword();
	}

	@Override
	public String getUsername() {
		return tbuser.getUsername();
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
	public boolean isEnabled() { return true; }
	
    /**
	 *  Tbuser Role 파싱하는 함수.
	 *  @return Collection<? extends GrantedAuthority> authorities
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		tbuser.getRoleList().forEach(tbuserRoleType->{
			authorities.add(()->tbuserRoleType.getRoleType().getTypeName());
		});
		return authorities;
	}

}