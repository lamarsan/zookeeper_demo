package com.lamarsan.zookeeper.commons;

/**
 * className: Constants
 * description: 常量定义
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 16:24
 */
public class Constants {
    public static final String ZK_SERVER_PATH = "101.132.43.140";
    public static final Integer TIMEOUT = 5000;
    public static final String START = "客户端开始连接zookeeper服务器...";
    public static final String STATE = "连接状态:";
    public static final String RECEIVE = "接收到watch通知:";
    public static final String RESTART = "开始会话重连";
    public static final String RESTART_STATE = "开始会话重连状态zkSession:";
    public static final String CONFIG_NODE_PATH = "/super/lamar";
    public static final String SUB_PATH = "/redis-config";
}
