package com.lamarsan.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.logging.Logger;

import static com.lamarsan.zookeeper.Constants.*;

/**
 * className: ZKConnectSessionWatcher
 * description: 使用sessionId会话重连
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 16:30
 */
public class ZKConnectSessionWatcher implements Watcher {
    private static final Logger log = Logger.getLogger("log");

    public static void main(String[] args) throws Exception{
        ZooKeeper zk = new ZooKeeper(ZK_SERVER_PATH, TIMEOUT, new ZKConnect());

        long sessionId = zk.getSessionId();
        String ssid = "0x" + Long.toHexString(sessionId);
        System.out.println(ssid);
        byte[] sessionPassword = zk.getSessionPasswd();

        log.info(START);
        log.info(STATE + zk.getState());

        Thread.sleep(1000);

        log.info(STATE + zk.getState());

        Thread.sleep(200);

        //开始会话重连
        log.info(RESTART);
        ZooKeeper zkSession = new ZooKeeper(ZK_SERVER_PATH, TIMEOUT, new ZKConnectSessionWatcher(), sessionId, sessionPassword);
        log.info(RESTART_STATE + zkSession.getState());

        Thread.sleep(1000);

        log.info(RESTART_STATE + zkSession.getState());
    }

    public void process(WatchedEvent event) {
        log.info(RECEIVE + event);
    }
}
