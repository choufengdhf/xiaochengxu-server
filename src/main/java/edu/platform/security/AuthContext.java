package edu.platform.security;

public class AuthContext {
  private static final ThreadLocal<String> OPENID = new ThreadLocal<>();

  public static void setOpenid(String openid) { OPENID.set(openid); }
  public static String getOpenid() { return OPENID.get(); }
  public static void clear() { OPENID.remove(); }
}