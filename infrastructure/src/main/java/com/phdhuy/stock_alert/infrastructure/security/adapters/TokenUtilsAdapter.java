package com.phdhuy.stock_alert.infrastructure.security.adapters;

import com.phdhuy.stock_alert.domain.auth.model.Token;
import com.phdhuy.stock_alert.domain.auth.ports.outbound.TokenUtilsPort;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.entity.UserEntity;
import com.phdhuy.stock_alert.infrastructure.databases.postgresql.repository.UserRepository;
import com.phdhuy.stock_alert.infrastructure.mapper.TokenMapper;
import com.phdhuy.stock_alert.infrastructure.security.config.TokenProperties;
import com.phdhuy.stock_alert.shared.constant.MessageConstant;
import com.phdhuy.stock_alert.shared.exception.ForbiddenException;
import com.phdhuy.stock_alert.shared.exception.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenUtilsAdapter implements TokenUtilsPort {

  private final TokenProperties tokenProperties;

  private final UserRepository userRepository;

  private final TokenMapper tokenMapper;

  public Token createToken(UUID userId) {
    return tokenMapper.toOauthAccessToken(
        this.createAccessToken(userId),
        this.createRefreshToken(userId),
        tokenProperties.getTokenExpirationMsec() / 1000);
  }

  public Token refreshToken(String refreshToken) {
    this.validateRefreshToken(refreshToken, tokenProperties.getRefreshTokenSecret());

    return tokenMapper.toOauthAccessToken(
        this.createAccessToken(
            this.getUUIDFromToken(refreshToken, tokenProperties.getRefreshTokenSecret())),
        refreshToken,
        tokenProperties.getTokenExpirationMsec() / 1000);
  }

  public UserEntity getUserFromToken(String token) {
    this.validateAccessToken(token, tokenProperties.getTokenSecret());
    return userRepository
        .findById(this.getUUIDFromToken(token, tokenProperties.getTokenSecret()))
        .orElseThrow(() -> new NotFoundException(MessageConstant.USER_NOT_FOUND));
  }

  private UUID getUUIDFromToken(String token, String secret) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    return UUID.fromString(claims.getSubject());
  }

  private String createAccessToken(UUID userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getTokenSecret())
        .compact();
  }

  private String createRefreshToken(UUID userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + tokenProperties.getRefreshTokenExpirationMsec());

    return Jwts.builder()
        .setSubject(userId.toString())
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenProperties.getRefreshTokenSecret())
        .compact();
  }

  private void validateAccessToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForbiddenException(MessageConstant.EXPIRED_TOKEN);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      throw new ForbiddenException(MessageConstant.INVALID_TOKEN);
    }
  }

  private void validateRefreshToken(String authToken, String secret) {
    try {
      Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
    } catch (ExpiredJwtException ex) {
      throw new ForbiddenException(MessageConstant.EXPIRED_REFRESH_TOKEN);
    } catch (Exception ex) {
      throw new ForbiddenException(MessageConstant.INVALID_REFRESH_TOKEN);
    }
  }
}
