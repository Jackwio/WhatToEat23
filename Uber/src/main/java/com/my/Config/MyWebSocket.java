package com.my.Config;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(value = "/websocket")    //声明这是一个Socket服务
public class MyWebSocket {
    //session为与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数
     * @throws Exception
     */
    @OnOpen
    public void onOpen(Session session) throws Exception {
        this.session = session;
        System.out.println("Open");
    }

    /**
     * 连接关闭调用的方法
     *
     * @throws Exception
     */
    @OnClose
    public void onClose() throws Exception {
        System.out.println("Close");
    }

    /**
     * 收到消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws Exception
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        if (message != null) {
            switch (message) {
                case "start":
                    System.out.println("接收到数据" + message);
                    sendMessage("哈哈哈哈哈哈哈哈");
                    break;
                case "question":
                case "close":
                    System.out.println("关闭连接");
                    onClose();
                default:
                    break;
            }
        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 发送消息方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);   //向客户端发送数据
    }

}

