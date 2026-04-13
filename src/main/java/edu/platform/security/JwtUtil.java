package edu.platform.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
  private final SecretKey key;

  public JwtUtil(String secret) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String sign(Map<String, Object> claims, long ttlMillis) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
      .claims(claims)
      .issuedAt(new Date(now))
      .expiration(new Date(now + ttlMillis))
      .signWith(key)
      .compact();
  }

  public Map<String, Object> verify(String token) {
    return Jwts.parser()
      .verifyWith(key)
      .build()
      .parseSignedClaims(token)
      .getPayload();
  }
}