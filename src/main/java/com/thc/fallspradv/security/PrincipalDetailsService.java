package com.thc.fallspradv.security;

import com.thc.fallspradv.domain.Tbuser;
import com.thc.fallspradv.exception.NoMatchingDataException;
import com.thc.fallspradv.repository.TbuserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {
	
	private final TbuserRepository tbuserRepository;
	
	public PrincipalDetailsService(TbuserRepository tbuserRepository) {
		this.tbuserRepository = tbuserRepository;
	}
	
    /**
	 *  principalDetails 생성을 위한 함수.
	 *  username으로 tbuser 조회, principalDetails 생성
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Tbuser tbuser = tbuserRepository.findByUsername(username);
		if(tbuser == null) {
			throw new NoMatchingDataException("username : " + username);
		}
		return new PrincipalDetails(tbuser);
	}
	
}