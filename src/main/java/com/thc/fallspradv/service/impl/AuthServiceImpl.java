package com.thc.fallspradv.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.thc.fallspradv.dto.RefreshTokenDto;
import com.thc.fallspradv.exception.InvalidTokenException;
import com.thc.fallspradv.repository.RefreshTokenRepository;
import com.thc.fallspradv.service.AuthService;
import com.thc.fallspradv.util.ExternalProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

	private final ExternalProperties externalProperties;
	private final RefreshTokenRepository refreshTokenRepository;
	public AuthServiceImpl(
			ExternalProperties externalProperties
			, RefreshTokenRepository refreshTokenRepository
	) {
		this.externalProperties = externalProperties;
		this.refreshTokenRepository = refreshTokenRepository;
	}

	@Override
	public Algorithm getTokenAlgorithm() {
		return Algorithm.HMAC512(externalProperties.getTokenSecretKey());
	}

	/**
	 * 	Access Token 생성을 위한 함수.
	 *  Payload에 tbuser Id를 담는다. 
	 *  
	 */
	@Override
	public String createAccessToken(String tbuserId) {
    	return JWT.create()
 			 	  .withSubject("accessToken")
 			 	  .withClaim("id", tbuserId)
 			 	  .withExpiresAt(new Date(System.currentTimeMillis()+externalProperties.getAccessTokenExpirationTime()))
 			 	  .sign(getTokenAlgorithm());
	}

    /**
	 * 	Access Token 검증을 위한 함수
	 *   
	 */
	@Override
	public String verifyAccessToken(String accessToken) throws JWTVerificationException {
		return JWT.require(getTokenAlgorithm())
				.build()
				.verify(accessToken)
				.getClaim("id").asString();
	}

    /**
	 * 	Refresh Token 생성을 위한 함수.
	 *  Payload에 tbuser Id를 담는다.
	 *  DB에 저장할수도 있음.
	 *  redis에 tbuserId를 key로, 발급한 Refresh Token을 value로 저장할 수도있음.
	 *  
	 */
	@Override
	public String createRefreshToken(String tbuserId) {

		// refreshToken 기존꺼 지우기 (로그인 하면 이전 리프레시 토큰 지우는 기능)
		revokeRefreshToken(tbuserId);

    	String refreshToken = JWT.create()
			     				 .withSubject("refreshToken")
			     				 .withClaim("id", tbuserId)
			     				 .withExpiresAt(new Date(System.currentTimeMillis()+externalProperties.getRefreshTokenExpirationTime()))
			     				 .sign(getTokenAlgorithm());

		//디비 저장
		refreshTokenRepository.save(new RefreshTokenDto.CreateReqDto(refreshToken, tbuserId).toEntity());

		return refreshToken;
	}

	/**
	 * 	Refresh Token 삭제 위한 함수.
	 *  tbuser Id로 조회해서 모두 지운다.
	 */
	@Override
	public void revokeRefreshToken(String tbuserId) {
		refreshTokenRepository.deleteAll(refreshTokenRepository.findByTbuserId(tbuserId));
	}

    /**
	 * 	Refresh Token 검증을 위한 함수
	 *   
	 */
	@Override
	public String verifyRefreshToken(String refreshToken) throws JWTVerificationException {

		refreshTokenRepository.findByContent(refreshToken)
				.orElseThrow(() -> new InvalidTokenException(""));

		return JWT.require(getTokenAlgorithm())
				.build()
				.verify(refreshToken)
				.getClaim("id").asString();
	}
	
	/**
	 * 	Access Token 발급을 위한 함수.
	 *  Refresh Token이 유효하다면 Access Token 발급.
	 *  
	 */
	@Override
	public String issueAccessToken(String refreshToken) throws JWTVerificationException {
		// Refresh Token 검증(실패시 throws JWTVerificationException)
		//System.out.println("refresh : " +refreshToken);
		String tbuserId = verifyRefreshToken(refreshToken);
		// Access Token 생성
		String accessToken = createAccessToken(tbuserId);
		return accessToken;
	}

}