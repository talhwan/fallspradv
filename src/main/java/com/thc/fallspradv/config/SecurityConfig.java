package com.thc.fallspradv.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thc.fallspradv.repository.TbuserRepository;
import com.thc.fallspradv.security.FilterExceptionHandlerFilter;
import com.thc.fallspradv.security.JwtAuthenticationFilter;
import com.thc.fallspradv.security.JwtAuthorizationFilter;
import com.thc.fallspradv.service.AuthService;
import com.thc.fallspradv.util.ExternalProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
@Configuration
public class SecurityConfig {
	
	private final TbuserRepository tbuserRepository;
	private final CorsFilterConfiguration corsFilterConfiguration;
	private final ObjectMapper objectMapper;
	private final AuthService authService; //토큰 팩토리랑 똑같은 것!!
	private final ExternalProperties externalProperties;

	public SecurityConfig(TbuserRepository tbuserRepository, CorsFilterConfiguration corsFilterConfiguration, ObjectMapper objectMapper, AuthService authService
			, ExternalProperties externalProperties) {
		this.tbuserRepository = tbuserRepository;
		this.corsFilterConfiguration = corsFilterConfiguration;
		this.objectMapper = objectMapper;
		this.authService = authService;
		this.externalProperties = externalProperties;
	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    /**
	 *  Spring Security 권한 설정.
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
					.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				//세션을 안쓰는 경우!!	STATELESS!!
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
					.formLogin(AbstractHttpConfigurer::disable)
					.httpBasic(AbstractHttpConfigurer::disable)
					.addFilter(corsFilterConfiguration.corsFilter())
				.apply(new CustomDsl());
		return http.build();
	}
					
	public class CustomDsl extends AbstractHttpConfigurer<CustomDsl, HttpSecurity> {
		
	    /**
		 *  Jwt Token Authentication을 위한 filter 설정.
		 *  
		 *  jwtAuthenticationFilter: 인증을 위한 필터("/api/login")
		 *  JwtAuthorizationFilter: 인가를 위한 필터
		 *  FilterExceptionHandlerFilter: TokenExpiredException 핸들링을 위한 필터 
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
			JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, objectMapper, authService, externalProperties);
			jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
			
			http.addFilter(corsFilterConfiguration.corsFilter())
				.addFilter(jwtAuthenticationFilter)
				.addFilter(new JwtAuthorizationFilter(authenticationManager, tbuserRepository, authService, externalProperties))
				.addFilterBefore(new FilterExceptionHandlerFilter(), BasicAuthenticationFilter.class);
		}
		
	}

}