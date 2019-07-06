package com.lamarsan.zookeeper.node;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.lamarsan.zookeeper.commons.Constants.TIMEOUT;
import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: ZKGetChildrenList
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 14:18
 */
public class ZKGetChildrenList implements Watcher {

    private ZooKeeper zooKeeper = null;

    public ZKGetChildrenList() {

    }

    public ZKGetChildrenList(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, TIMEOUT, new ZKGetChildrenList());
        } catch (IOException e) {
            e.printStackTrace();
            if (zooKeeper != null) {
                try{
                    zooKeeper.close();
                }catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        ZKGetNodeData zkServer = new ZKGetNodeData(ZK_SERVER_PATH);
        //List<String> strChildList = zkServer.getZooKeeper().getChildren("/testnode", true);
        //for (String s : strChildList) {
        //    System.out.println(s);
        //}

        //异步调用
        String ctx = "{'callback':'ChildrenCallback'}";
        //zkServer.getZooKeeper().getChildren("/testnode", true, new AsyncCallback.ChildrenCallback() {
        //    public void processResult(int rc, String path, Object ctx, List<String> children) {
        //        for (String s : children) {
        //            System.out.println(s);
        //        }
        //        System.out.println("ChildrenCallback:" + path);
        //        System.out.println((String) ctx);
        //    }
        //}, ctx);
        zkServer.getZooKeeper().getChildren("/testnode", true, new AsyncCallback.Children2Callback() {
            public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
                for (String s : children) {
                    System.out.println(s);
                }
                System.out.println("ChildrenCallback" + path);
                System.out.println((String) ctx);
                System.out.println(stat.toString());
            }
        }, ctx);

        countDownLatch.await();
    }

    public void process(WatchedEvent event) {
        try {
            if (event.getType() == Event.EventType.NodeChildrenChanged) {
                System.out.println("NodeChildrenChanged");
                ZKGetChildrenList zkServer = new ZKGetChildrenList(ZK_SERVER_PATH);
                List<String> strChildList = zkServer.getZooKeeper().getChildren(event.getPath(), false);
                for (String s : strChildList) {
                    System.out.println(s);
                }
                countDownLatch.countDown();
            } else if (event.getType() == Event.EventType.NodeCreated) {
                System.out.println("NodeCreated");
            } else if (event.getType() == Event.EventType.NodeDataChanged) {
                System.out.println("NodeDataChanged");
            } else if (event.getType() == Event.EventType.NodeDeleted) {
                System.out.println("NodeDeleted");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        ZKGetChildrenList.countDownLatch = countDownLatch;
    }
}
