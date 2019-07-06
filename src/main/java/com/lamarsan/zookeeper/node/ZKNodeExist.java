package com.lamarsan.zookeeper.node;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.lamarsan.zookeeper.commons.Constants.TIMEOUT;
import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: ZKNodeExist
 * description: 查看结点是否存在
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 15:11
 */
public class ZKNodeExist implements Watcher {

    private ZooKeeper zooKeeper = null;

    public ZKNodeExist() {

    }

    public ZKNodeExist(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, TIMEOUT, new ZKNodeExist());
        } catch (IOException e) {
            e.printStackTrace();
            if (zooKeeper != null) {
                try {
                    zooKeeper.close();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZKNodeExist zkServer = new ZKNodeExist(ZK_SERVER_PATH);

        Stat stat = zkServer.getZooKeeper().exists("/testnode", true);
        if (stat != null) {
            System.out.println("查询的结点版本为dataVersion:" + stat.getVersion());
        } else {
            System.out.println("该结点不存在..");
        }
        countDownLatch.await();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeCreated) {
            System.out.println("结点创建");
            countDownLatch.countDown();
        } else if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
            System.out.println("结点数据改变");
            countDownLatch.countDown();
        } else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            System.out.println("结点删除");
            countDownLatch.countDown();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public static void setCountDownLatch(CountDownLatch countDownLatch) {
        ZKNodeExist.countDownLatch = countDownLatch;
    }
}
