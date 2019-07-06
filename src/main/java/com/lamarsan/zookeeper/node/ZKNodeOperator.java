package com.lamarsan.zookeeper.node;

import com.lamarsan.zookeeper.commons.Constants;
import com.lamarsan.zookeeper.commons.CreateCallBack;
import com.lamarsan.zookeeper.commons.DeleteCallBack;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.List;

import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: ZKNodeOperator
 * description: 修改结点
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 16:50
 */
public class ZKNodeOperator implements Watcher {
    private ZooKeeper zooKeeper = null;

    public ZKNodeOperator() {

    }

    public ZKNodeOperator(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, Constants.TIMEOUT, new ZKNodeOperator());
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

    public static void main(String[] args) throws Exception{
        ZKNodeOperator zkServer = new ZKNodeOperator(ZK_SERVER_PATH);
        //创建结点
        //zkServer.createZNode("/testnode", "testnode".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);
        //修改结点
        //Stat status = zkServer.getZookeeper().setData("/testnode", "xyz".getBytes(), 0);
        //System.out.println(status.getVersion());
        //删除结点
        //同步
        //zkServer.getZookeeper().delete("/testnode",1);
        //异步
        String ctx = "{'delete';'success'}";
        zkServer.getZookeeper().delete("/testnode", 0, new DeleteCallBack(), ctx);
        Thread.sleep(2000);
    }


    /**
     * 创建结点
     * @param path
     * @param data
     * @param acls
     */
    public void createZNode(String path, byte[] data, List<ACL> acls) throws InterruptedException {
        //同步创建
        //String result = "";
        //try {
        //    //PERSISTENT:持久结点，EPHEMERAL:临时结点
        //    result = zooKeeper.create(path, data, acls, CreateMode.EPHEMERAL);
        //    System.out.println("创建结点：\t" + result + "\t成功...");
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //异步创建
        String ctx = "{'create':'success}";
        zooKeeper.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallBack(), ctx);
        //System.out.println("创建结点：\t" + result + "\t成功...");
        Thread.sleep(2000);
    }

    public ZooKeeper getZookeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }

    public void process(WatchedEvent event) {
        System.out.println(event);
    }
}
