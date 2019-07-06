package com.lamarsan.zookeeper.connect;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.logging.Logger;

import static com.lamarsan.zookeeper.commons.Constants.*;

/**
 * className: ZKConnect
 * description: 连接远程zookeeper
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 15:51
 */
public class ZKConnect implements Watcher {
    private static final Logger log = Logger.getLogger("log");

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = new ZooKeeper(ZK_SERVER_PATH, TIMEOUT, new ZKConnect());
        log.info(START);
        log.info(STATE + zk.getState());

        Thread.sleep(2000);

        log.info(STATE + zk.getState());
    }

    public void process(WatchedEvent event) {
        log.info(RECEIVE + event);
    }
}
