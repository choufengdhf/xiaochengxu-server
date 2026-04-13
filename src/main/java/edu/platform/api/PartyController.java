package edu.platform.api;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/party")
public class PartyController {

  @GetMapping("/progress/me")
  public Map<String, Object> me() {
    List<Map<String, Object>> timeline = new ArrayList<>();
    timeline.add(Map.of("nodeId", 1, "nodeName", "申请人", "status", "已完成", "time", "2026-03-01"));
    timeline.add(Map.of("nodeId", 2, "nodeName", "积极分子", "status", "进行中", "time", "2026-04-01"));
    timeline.add(Map.of("nodeId", 3, "nodeName", "发展对象", "status", "未开始", "time", ""));

    return Map.of(
      "currentNodeName", "积极分子",
      "nextNodeName", "发展对象",
      "timeline", timeline
    );
  }

  public static class SubmitReq {
    public Long fileId;
    public String remark;
  }

  @PostMapping("/materials/submit")
  public Map<String, Object> submit(@RequestBody SubmitReq req) {
    return Map.of("ok", true);
  }
}