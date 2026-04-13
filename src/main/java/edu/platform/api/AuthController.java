package edu.platform.api;

import edu.platform.security.JwtUtil;
import edu.platform.store.Store;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {
  private final JwtUtil jwt;

  public AuthController(JwtUtil jwt) { this.jwt = jwt; }

  public static class WxLoginReq { @NotBlank public String code; }

  @PostMapping("/wx-login")
  public Map<String, Object> wxLogin(@RequestBody WxLoginReq req) {
    String openid = "openid_" + Integer.toHexString(req.code.getBytes(StandardCharsets.UTF_8).hashCode());

    Map<String, Object> claims = new HashMap<>();
    claims.put("openid", openid);
    String token = jwt.sign(claims, 7L * 24 * 3600 * 1000);

    Map<String, Object> user = Store.USERS_BY_OPENID.get(openid);
    boolean bound = user != null && user.get("studentNo") != null;

    Map<String, Object> resp = new HashMap<>();
    resp.put("token", token);
    resp.put("userBound", bound);
    resp.put("user", user);
    return resp;
  }
}