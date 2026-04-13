package edu.platform.api;

import edu.platform.store.Store;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

  @GetMapping
  public Map<String, Object> list() {
    List<Map<String, Object>> items = Store.NOTICES.stream()
      .sorted((a,b)-> Boolean.compare((Boolean)b.get("pinned"), (Boolean)a.get("pinned")))
      .collect(Collectors.toList());
    return Map.of("items", items);
  }

  @GetMapping("/{id}")
  public Map<String, Object> detail(@PathVariable int id) {
    return Store.NOTICES.stream()
      .filter(x -> ((Number)x.get("id")).intValue() == id)
      .findFirst()
      .orElseThrow();
  }

  @PostMapping("/{id}/read")
  public Map<String, Object> read(@PathVariable int id) {
    return Map.of("ok", true);
  }
}