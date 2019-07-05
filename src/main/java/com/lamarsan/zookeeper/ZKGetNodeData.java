package com.lamarsan.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static com.lamarsan.zookeeper.Constants.*;
/**
 * className: ZKGetNodeData
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/5 21:46
 */
public class ZKGetNodeData implements Watcher {
    private ZooKeeper zooKeeper = null;

    private static Stat stat = new Stat();

    public ZKGetNodeData() {

    }

    public ZKGetNodeData(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, TIMEOUT, new ZKGetNodeData());
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
        ZKGetNodeData zkServer = new ZKGetNodeData(ZK_SERVER_PATH);
        byte[] resByte = zkServer.getZooKeeper().getData("/testnode", true, stat);
        String result = new String(resByte);
        System.out.println("当前值" + result);
        countDownLatch.await();
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public static Stat getStat() {
        return stat;
    }

    public static void setStat(Stat stat) {
        ZKGetNodeData.stat = stat;
    }

    public static CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public static void setCountDownLatch(CountDownLatch countDownLatch) {
        ZKGetNodeData.countDownLatch = countDownLatch;
    }

    public void process(WatchedEvent watchedEvent) {
        try {
            if (watchedEvent.getType() == Event.EventType.NodeDataChanged) {
                ZKGetNodeData zkServer = new ZKGetNodeData(ZK_SERVER_PATH);
                byte[] resByte = zkServer.getZooKeeper().getData("/testnode", false, stat);
                String result = new String(resByte);
                System.out.println("更改后的值：" + result);
                System.out.println("版本号变化dversion:" + stat.getVersion());
                //让主线程结点继续执行
                countDownLatch.countDown();
            }
            else if (watchedEvent.getType() == Event.EventType.NodeCreated) {
                //TODO
            }
            else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                //TODO
            }
            else if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                //TODO
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
