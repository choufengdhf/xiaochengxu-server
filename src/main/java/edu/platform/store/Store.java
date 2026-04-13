package edu.platform.store;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Store {
  public static final Map<String, Map<String, Object>> USERS_BY_OPENID = new ConcurrentHashMap<>();
  public static final AtomicLong ID = new AtomicLong(1000);

  public static final List<Map<String, Object>> QA = new ArrayList<>();
  public static final List<Map<String, Object>> NOTICES = new ArrayList<>();
  public static final List<Map<String, Object>> TICKET_TYPES = new ArrayList<>();
  public static final Map<String, List<Map<String, Object>>> TICKETS_BY_OPENID = new ConcurrentHashMap<>();

  static {
    QA.add(new HashMap<>(Map.of(
      "id", 1,
      "title", "入党申请书提交要求？",
      "summary", "说明提交材料与时间线",
      "answer", "请提交纸质申请书，并在系统中上传电子版材料；具体节点要求以学院最新政策为准。",
      "tags", List.of("党团事务"),
      "categoryName", "党团",
      "policyUrl", "https://example.com/policy"
    )));
    QA.add(new HashMap<>(Map.of(
      "id", 2,
      "title", "奖学金评定标准在哪里看？",
      "summary", "提供标准与附件链接",
      "answer", "请查看信息学院奖学金评定办法（最新版），如有更新以学院通知为准。",
      "tags", List.of("奖助学金"),
      "categoryName", "奖助"
    )));

    NOTICES.add(new HashMap<>(Map.of(
      "id", 1,
      "title", "关于2026春季安全教育的通知",
      "content", "请各班按要求完成安全教育学习。",
      "department", "学生科",
      "publishTime", "2026-04-10 10:00",
      "tags", List.of("活动"),
      "pinned", true
    )));
    NOTICES.add(new HashMap<>(Map.of(
      "id", 2,
      "title", "2026届就业材料提交提醒",
      "content", "请在规定时间内提交就业协议相关材料。",
      "department", "就业办",
      "publishTime", "2026-04-12 09:30",
      "tags", List.of("就业"),
      "pinned", false
    )));

    TICKET_TYPES.add(new HashMap<>(Map.of("id", 1, "name", "在读证明")));
    TICKET_TYPES.add(new HashMap<>(Map.of("id", 2, "name", "请假申请")));
    TICKET_TYPES.add(new HashMap<>(Map.of("id", 3, "name", "盖章申请")));
  }
}