package edu.platform.api;

import edu.platform.security.AuthContext;
import edu.platform.store.Store;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class BindController {

  public static class BindReq {
    @NotBlank public String studentNo;
    @NotBlank public String name;
    public String major;
  }

  @PostMapping("/bind-student")
  public Map<String, Object> bind(@RequestBody BindReq req) {
    String openid = AuthContext.getOpenid();
    if (openid == null) throw new RuntimeException("unauthorized");

    Map<String, Object> user = new HashMap<>();
    user.put("openid", openid);
    user.put("studentNo", req.studentNo);
    user.put("name", req.name);
    user.put("major", req.major == null ? "" : req.major);
    user.put("role", "STUDENT");

    Store.USERS_BY_OPENID.put(openid, user);
    return Map.of("user", user);
  }
}