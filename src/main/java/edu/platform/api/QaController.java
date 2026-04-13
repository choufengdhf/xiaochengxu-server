package edu.platform.api;

import edu.platform.store.Store;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/qa")
public class QaController {

  @GetMapping("/hot")
  public Map<String, Object> hot() {
    return Map.of("items", Store.QA);
  }

  @GetMapping("/search")
  public Map<String, Object> search(@RequestParam String q) {
    String qq = q.trim().toLowerCase();
    List<Map<String, Object>> items = Store.QA.stream()
      .filter(x -> ((String)x.get("title")).toLowerCase().contains(qq) ||
                   ((String)x.get("answer")).toLowerCase().contains(qq))
      .collect(Collectors.toList());
    return Map.of("items", items);
  }

  @GetMapping("/articles/{id}")
  public Map<String, Object> detail(@PathVariable int id) {
    return Store.QA.stream()
      .filter(x -> ((Number)x.get("id")).intValue() == id)
      .findFirst()
      .orElseThrow();
  }
}