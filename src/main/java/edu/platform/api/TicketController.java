package edu.platform.api;

import edu.platform.security.AuthContext;
import edu.platform.store.Store;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class TicketController {

  @GetMapping("/ticket-types")
  public Map<String, Object> types() {
    return Map.of("items", Store.TICKET_TYPES);
  }

  @GetMapping("/tickets")
  public Map<String, Object> list() {
    String openid = AuthContext.getOpenid();
    List<Map<String, Object>> items = Store.TICKETS_BY_OPENID.getOrDefault(openid, new ArrayList<>());
    return Map.of("items", items);
  }

  public static class CreateReq {
    public Integer typeId;
    public String reason;
  }

  @PostMapping("/tickets")
  public Map<String, Object> create(@RequestBody CreateReq req) {
    String openid = AuthContext.getOpenid();
    Map<String, Object> type = Store.TICKET_TYPES.stream()
      .filter(x -> x.get("id").equals(req.typeId))
      .findFirst().orElseThrow();

    long id = Store.ID.incrementAndGet();
    Map<String, Object> ticket = new HashMap<>();
    ticket.put("id", id);
    ticket.put("typeId", req.typeId);
    ticket.put("typeName", type.get("name"));
    ticket.put("reason", req.reason);
    ticket.put("status", "待审批");
    ticket.put("createTime", "2026-04-13 12:00");
    ticket.put("logs", List.of(Map.of("action", "提交申请", "time", "2026-04-13 12:00", "by", "学生")));

    Store.TICKETS_BY_OPENID.computeIfAbsent(openid, k -> new ArrayList<>()).add(0, ticket);
    return Map.of("id", id);
  }

  @GetMapping("/tickets/{id}")
  public Map<String, Object> detail(@PathVariable long id) {
    String openid = AuthContext.getOpenid();
    return Store.TICKETS_BY_OPENID.getOrDefault(openid, List.of()).stream()
      .filter(x -> ((Number)x.get("id")).longValue() == id)
      .findFirst().orElseThrow();
  }
}