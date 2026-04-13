package edu.platform.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class AuthInterceptor implements HandlerInterceptor {

  private final JwtUtil jwt;

  public AuthInterceptor(JwtUtil jwt) { this.jwt = jwt; }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String path = request.getRequestURI();

    // 放行登录/绑定接口；文件下载这里也放行（也可改成必须登录再下）
    if (path.startsWith("/api/auth/") || (path.startsWith("/api/files/") && path.endsWith("/download"))) {
      return true;
    }

    String auth = request.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
      response.setStatus(401);
      return false;
    }
    String token = auth.substring("Bearer ".length());
    try {
      Map<String, Object> claims = jwt.verify(token);
      String openid = (String) claims.get("openid");
      AuthContext.setOpenid(openid);
      return true;
    } catch (Exception e) {
      response.setStatus(401);
      return false;
    }
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    AuthContext.clear();
  }
}