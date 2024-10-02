package com.thc.fallspradv.security;

import com.thc.fallspradv.domain.Tbuser;
import com.thc.fallspradv.exception.NoAuthorizationException;
import com.thc.fallspradv.exception.NoMatchingDataException;
import com.thc.fallspradv.repository.TbuserRepository;
import com.thc.fallspradv.service.AuthService;
import com.thc.fallspradv.util.ExternalProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.function.Supplier;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final TbuserRepository tbuserRepository;
	private final AuthService authService;
	private final ExternalProperties externalProperties;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TbuserRepository tbuserRepository, AuthService authService
			, ExternalProperties externalProperties
	) {
		super(authenticationManager);
		this.tbuserRepository = tbuserRepository;
		this.authService = authService;
		this.externalProperties = externalProperties;
	}
	
	/**
     *  권한 인가를 위한 함수.
     *  Access Token을 검증하고 유효하면 Authentication을 직접 생성해 SecurityContextHolder에 넣는다.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("request!!! : " + request);
		String jwtHeader = request.getHeader(externalProperties.getAccessKey());
		if(jwtHeader == null || !jwtHeader.startsWith(externalProperties.getTokenPrefix())) {
			// 토큰 없을 시 Authentication 없는 상태로 doFilter -> Spring Security가 잡아낸다.
			System.out.println("jwtHeader null");
			chain.doFilter(request, response);
			return;
		}
		String accessToken = jwtHeader.replace(externalProperties.getTokenPrefix(), "");
		System.out.println("accessToken!!! : " + accessToken);
		String tbuserId = authService.verifyAccessToken(accessToken);
		System.out.println("tbuserId : " + tbuserId);

		// 유저 조회, 없을 시 return NoMatchingDataException(404)
		Tbuser tbuserEntity = tbuserRepository.findEntityGraphRoleTypeById(tbuserId).orElseThrow(new Supplier<NoMatchingDataException>() {
			@Override
			public NoMatchingDataException get() {
				return new NoMatchingDataException("id : " + tbuserId);
			}
		});

		String process = tbuserEntity.getProcess();
		logger.info("process : " + process);
		//if("blocked".equals(process)) { throw new NoAuthorizationException("blocked : " + tbuserId); }


		PrincipalDetails principalDetails = new PrincipalDetails(tbuserEntity);
		// Authentication 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
		// SecurityContextHolder에 Authentication을 담아서 Spring Security가 권한 처리 할 수 있게 한다.
		SecurityContextHolder.getContext().setAuthentication(authentication);

		/*
		//사용자 정보 추출을 위한 코드
		response.setHeader("tbuserId", tbuserId);
		System.out.println("tbuserId!!! : " + tbuserId);
		*/

		/*
		사용자 단계에 따른 판단
		*/
		/*
		String tbuserProcess = tbuserEntity.getProcess();
		System.out.println("tbuserProcess!!! : " + tbuserProcess);
		response.setHeader("tbuserProcess", tbuserProcess);
		 */

		chain.doFilter(request, response);

	}

}