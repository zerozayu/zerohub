package com.zerozayu.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ws
 *
 * @author zhangyu
 * @date 2024/1/3 21:39
 */
@Slf4j
@Component
@ServerEndpoint(value = "/ws/{userId}")
public class WebSocketServer {
    // 存储客户端session信息
    private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    // 存储把不同用户的客户端session信息集合
    private static final Map<String, Set<String>> connection = new ConcurrentHashMap<>();

    // 会话id
    private String sid = null;

    // 建立连接的用户
    private String userId;

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.sid = UUID.randomUUID().toString();
        this.userId = userId;
        clients.put(this.sid, session);
        // 判断该用户是否存在会话信息,不存在则添加
        Set<String> clientSet = connection.computeIfAbsent(userId, k -> new HashSet<>());

        clientSet.add(this.sid);
        log.info("{} 用户建立连接,{} 连接开启", this.userId, this.sid);
    }

    @OnClose
    public void onClose() {
        clients.remove(this.sid);
        log.info("{} 连接断开", this.sid);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自用户:{}的信息{}", this.userId, message);

    }

    @OnError
    public void onError(Throwable error) {
        log.error("系统错误: {}", error.getMessage());
        error.printStackTrace();
    }
}
