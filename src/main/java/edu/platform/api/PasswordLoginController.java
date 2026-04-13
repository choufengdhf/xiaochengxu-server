package edu.platform.api;

import edu.platform.security.JwtUtil;
import edu.platform.store.Store;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class PasswordLoginController {

  private final JwtUtil jwt;

  // 硬编码测试账号
  private static final String TEST_STUDENT_NO = "2020123456";
  private static final String TEST_PASSWORD = "123456";

  public PasswordLoginController(JwtUtil jwt) {
    this.jwt = jwt;
  }

  public static class PasswordLoginReq {
    @NotBlank public String studentNo;
    @NotBlank public String password;
  }

  @PostMapping("/password-login")
  public Map<String, Object> passwordLogin(@RequestBody PasswordLoginReq req) {
    if (!TEST_STUDENT_NO.equals(req.studentNo) || !TEST_PASSWORD.equals(req.password)) {
      // 统一返回 200 + ok=false，前端好处理；你也可以改成 401
      return Map.of("ok", false, "message", "学号或密码错误");
    }

    // 固定 openid（仅用于测试）
    String openid = "openid_test_password_001";

    Map<String, Object> user = new HashMap<>();
    user.put("openid", openid);
    user.put("studentNo", TEST_STUDENT_NO);
    user.put("name", "测试用户");
    user.put("major", "计算机科学与技术");
    user.put("role", "STUDENT");
    Store.USERS_BY_OPENID.put(openid, user);

    String token = jwt.sign(Map.of("openid", openid), 7L * 24 * 3600 * 1000);

    Map<String, Object> resp = new HashMap<>();
    resp.put("ok", true);
    resp.put("token", token);
    resp.put("userBound", true);
    resp.put("user", user);
    return resp;
  }
}