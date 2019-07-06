package com.lamarsan.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;

import static com.lamarsan.zookeeper.commons.Constants.ZK_SERVER_PATH;

/**
 * className: CuratorOperator
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 19:40
 */
public class CuratorOperator {
    public CuratorFramework client = null;

    public CuratorOperator() {
        /**
         * 同步创建zk实例，原生api为异步
         * 第一个参数为重试时间
         * 第二个参数为时间内最大的重试次数,超过未连接就不再重试
         */
        //RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        /**
         * n：重试的次数
         * sleepMsBetweenRetries：每次重试间隔的时间
         */
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);

        /**
         * 参数为每次重试间隔时间
         * 不推荐
         */
        //RetryPolicy retryPolicy = new RetryOneTime(3000);

        /**
         * 永远重试，不推荐
         */
        //RetryPolicy retryPolicy = new RetryForever(retryIntervalMs);

        /**
         * maxElapsedTimeMs：最大重试时间
         * sleepMsBetweenReties：每次重试间隔
         * 重试时间超过maxElapsedTimeMs后就不再重试
         */
        //RetryPolicy retryPolicy = new RetryUntilElapsed(2000, 3000);

        client = CuratorFrameworkFactory.builder().
                connectString(ZK_SERVER_PATH).
                sessionTimeoutMs(10000).
                retryPolicy(retryPolicy).
                namespace("workspace").
                build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //实例化
        CuratorOperator cto = new CuratorOperator();
        boolean isZKCuratorStarted = cto.client.isStarted();
        System.out.println("当前客户端状态：" + (isZKCuratorStarted ? "连接中" : "已关闭"));

        //创建结点
        String nodePath = "/super/lamar";
        //byte[] data = "superme".getBytes();
        //cto.client.create().creatingParentsIfNeeded().
        //        withMode(CreateMode.PERSISTENT).
        //        withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
        //        .forPath(nodePath, data);

        //更新结点数据
        //byte[] newData = "batman".getBytes();
        //cto.client.setData()
        //        .withVersion(0)
        //        .forPath(nodePath, newData);

        //删除结点
        //cto.client.delete()
        //        .guaranteed()                        //如果删除失败，那么会继续删除，直到成功
        //        .deletingChildrenIfNeeded()          //如果有子节点，就删除
        //        .withVersion(1)
        //        .forPath(nodePath);

        //读取结点数据
        //Stat stat = new Stat();
        //byte[] data = cto.client.getData().storingStatIn(stat).forPath(nodePath);
        //System.out.println("结点" + nodePath + "的数据为:" + new String(data));
        //System.out.println("该结点的版本号为：" + stat.getVersion());

        //查询子节点
        //List<String> childNodes = cto.client.getChildren().forPath(nodePath);
        //System.out.println("开始打印子节点：");
        //for (String s : childNodes) {
        //    System.out.println(s);
        //}

        //判断结点是否存在，如果不存在则为空
        //Stat statExist = cto.client.checkExists().forPath(nodePath + "/abc");
        //System.out.println(statExist);

        //watcher事件 当使用usingWatcher的时候，监听只会触发一次，监听完毕后就销毁
        //cto.client.getData().usingWatcher(new MyCuratorWathcer()).forPath(nodePath);
        //cto.client.getData().usingWatcher(new MyWatcher()).forPath(nodePath);

        //为结点添加watcher
        //final NodeCache nodeCache = new NodeCache(cto.client, nodePath);
        //nodeCache.start(true);
        //if (nodeCache.getCurrentData() != null) {
        //    System.out.println("结点初始化数据为：" + new String(nodeCache.getCurrentData().getData()));
        //} else {
        //    System.out.println("结点初始化数据为空");
        //}
        //nodeCache.getListenable().addListener(new NodeCacheListener() {
        //    @Override
        //    public void nodeChanged() throws Exception {
        //        String data = new String(nodeCache.getCurrentData().getData());
        //        System.out.println("结点路径：" + nodeCache.getCurrentData().getPath() + "数据：" + data);
        //    }
        //});

        //为子节点添加watcher
        //PathChildrenCache:监听数据结点的增删改，会触发事件
        String childNodePathCache = nodePath;
        //cacheData:设置缓存结点的数据状态
        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, childNodePathCache, true);
        /**
         * POST_INITIALIZED_EVENT:异步初始化，初始化之后会触发事件
         * NORMAL：异步初始化
         * BUILD_INITIAL_CACHE：同步初始化
         */
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);

        List<ChildData> childDataList = childrenCache.getCurrentData();
        System.out.println("当前数据结点的子节点数据列表：");
        for (ChildData cd : childDataList) {
            String childData = new String(cd.getData());
            System.out.println(childData);
        }

        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    System.out.println("子节点初始化OK");
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    System.out.println("添加子节点：" + pathChildrenCacheEvent.getData().getPath());
                    System.out.println("子节点数据：" + new String(pathChildrenCacheEvent.getData().getData()));
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    System.out.println("删除子节点：" + pathChildrenCacheEvent.getData().getPath());
                } else if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    System.out.println("修改子节点路径：" + pathChildrenCacheEvent.getData().getPath());
                    System.out.println("修改子节点数据：" + new String(pathChildrenCacheEvent.getData().getData()));
                }
            }
        });
        Thread.sleep(100000000);
        cto.closeZKClient();
        boolean isZKCuratorStarted2 = cto.client.isStarted();
        System.out.println("当前客户端状态：" + (isZKCuratorStarted2 ? "连接中" : "已关闭"));
    }
}
