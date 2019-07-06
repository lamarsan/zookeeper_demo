package com.lamarsan.zookeeper.acl;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

import static com.lamarsan.zookeeper.commons.Constants.TIMEOUT;
import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: ZKNodeAcl
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 15:51
 */
public class ZKNodeAcl implements Watcher {
    private ZooKeeper zooKeeper = null;

    public ZKNodeAcl() {

    }

    public ZKNodeAcl(String connectString) {
        try {
            zooKeeper = new ZooKeeper(connectString, TIMEOUT, new ZKNodeAcl());
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

    public void createZKNode(String path, byte[] data, List<ACL> acls) {
        String result = "";
        try {
            result = zooKeeper.create(path, data, acls, CreateMode.PERSISTENT);
            System.out.println("创建结点：\t" + result + "\t成功...");
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ZKNodeAcl zkServer = new ZKNodeAcl(ZK_SERVER_PATH);
        //任何人都能访问
        //zkServer.createZKNode("/acltestnode", "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE);

        //自定义用户认证访问
        //List<ACL> acls = new ArrayList<ACL>();
        //Id lamar1 = new Id("digest", AclUtils.getDigestUserPwd("lamar1:123456"));
        //Id lamar2 = new Id("digest", AclUtils.getDigestUserPwd("lamar2:123456"));
        //acls.add(new ACL(ZooDefs.Perms.ALL, lamar1));
        //acls.add(new ACL(ZooDefs.Perms.READ, lamar2));
        //acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, lamar2));
        //zkServer.createZKNode("/acltestnode/testdigest", "testdigest".getBytes(), acls);

        //注册过的用户必须通过addAuthInfo才能操作结点，参考命令addauth
        //zkServer.getZooKeeper().addAuthInfo("digest", "lamar2:123456".getBytes());
        //zkServer.createZKNode("/acltestnode/testdigest/childtest", "childtest".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL);
        //读取
        //Stat stat = new Stat();
        //byte[] data = zkServer.getZooKeeper().getData("/acltestnode/testdigest", false, stat);
        //System.out.println(new String(data));
        //设置值无权限
        //zkServer.getZooKeeper().setData("/acltestnode/testdigest", "123456".getBytes(), 0);

        //ip方式的acl
        //List<ACL> aclsIP = new ArrayList<ACL>();
        //Id ipId1 = new Id("ip", "192.168.3.239");
        //aclsIP.add(new ACL(ZooDefs.Perms.ALL, ipId1));
        //zkServer.createZKNode("/acltestnode/iptest2", "iptest".getBytes(), aclsIP);

        //验证ip是否有权限
        //zkServer.getZooKeeper().setData("/acltestnode/iptest2", "123456".getBytes(), 0);
        Stat stat = new Stat();
        byte[] data = zkServer.getZooKeeper().getData("/acltestnode/iptest2", false, stat);
        System.out.println(new String(data));
        System.out.println(stat.getVersion());
    }

    @Override
    public void process(WatchedEvent event) {

    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setZooKeeper(ZooKeeper zooKeeper) {
        this.zooKeeper = zooKeeper;
    }
}
