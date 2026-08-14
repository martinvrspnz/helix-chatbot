package com.bbva.chatbot.helix.util;

public final class UserContextHolder {

    private static final ThreadLocal<String> currentUsuarioId = new ThreadLocal<>();

    private UserContextHolder() {
    }

    public static void setUsuarioId(String usuarioId) {
        currentUsuarioId.set(usuarioId);
    }

    public static String getUsuarioId() {
        return currentUsuarioId.get();
    }

    public static void clear() {
        currentUsuarioId.remove();
    }
}
