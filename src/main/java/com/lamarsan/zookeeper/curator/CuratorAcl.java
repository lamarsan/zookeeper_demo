package com.lamarsan.zookeeper.curator;

import com.lamarsan.zookeeper.commons.AclUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.util.ArrayList;
import java.util.List;

import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: CuratorAcl
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 22:07
 */
public class CuratorAcl {
    public CuratorFramework client = null;

    public CuratorAcl() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .authorization("digest","lamar1:123456".getBytes())
                .connectString(ZK_SERVER_PATH)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception{
        //实例化
        CuratorAcl cto = new CuratorAcl();
        boolean isZKCuratorStarted = cto.client.isStarted();
        System.out.println("当前客户端的状态：" + (isZKCuratorStarted ? "连接中" : "已关闭"));

        String nodePath = "/acl/lamar";

        //自定义用户认证访问
        List<ACL> acls = new ArrayList<ACL>();
        Id lamar1 = new Id("digest", AclUtils.getDigestUserPwd("lamar1:123456"));
        Id lamar2 = new Id("digest", AclUtils.getDigestUserPwd("lamar2:123456"));
        acls.add(new ACL(ZooDefs.Perms.ALL, lamar1));
        acls.add(new ACL(ZooDefs.Perms.READ, lamar2));
        acls.add(new ACL(ZooDefs.Perms.DELETE | ZooDefs.Perms.CREATE, lamar2));

        //创建结点
        //byte[] data = "spiderman".getBytes();
        //cto.client.create().creatingParentsIfNeeded()
        //        .withMode(CreateMode.PERSISTENT)
        //        .withACL(acls)
        //        .forPath(nodePath,data);

        cto.client.setACL().withACL(acls).forPath("/acl");
        Thread.sleep(1000);
        cto.closeZKClient();
        boolean isZKCuratorStarted2 = cto.client.isStarted();
        System.out.println("当前客户端状态：" + (isZKCuratorStarted2 ? "连接中" : "已关闭"));
    }
}
