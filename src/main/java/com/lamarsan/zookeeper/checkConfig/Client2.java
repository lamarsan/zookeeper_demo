package com.lamarsan.zookeeper.checkConfig;

import com.lamarsan.zookeeper.util.JsonUtil;
import com.lamarsan.zookeeper.util.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.CountDownLatch;

import static com.lamarsan.zookeeper.commons.Constants.*;

/**
 * className: Client2
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 21:15
 */
public class Client2 {
    public CuratorFramework client = null;

    public Client2() {
        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
        client = CuratorFrameworkFactory.builder()
                .connectString(ZK_SERVER_PATH)
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                .namespace("workspace").build();
        client.start();
    }

    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }

    public static CountDownLatch countDownLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception{
        Client1 cto = new Client1();
        System.out.println("client2启动成功...");

        final PathChildrenCache childrenCache = new PathChildrenCache(cto.client, CONFIG_NODE_PATH, true);
        childrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);

        //添加监听事件
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    String configNodePath = pathChildrenCacheEvent.getData().getPath();
                    if (configNodePath.equals(CONFIG_NODE_PATH + SUB_PATH)) {
                        System.out.println("监听到配置发生变化，结点路径为：" + configNodePath);

                        //读取结点数据
                        String jsonConfig = new String(pathChildrenCacheEvent.getData().getData());
                        System.out.println("结点" + CONFIG_NODE_PATH + "的数据为：" + jsonConfig);

                        //从json转换配置
                        RedisConfig redisConfig = null;
                        if (StringUtils.isNotBlank(jsonConfig)) {
                            redisConfig = JsonUtil.getObject(jsonConfig, RedisConfig.class);
                        }

                        //配置不为空则进行相应操作
                        if (redisConfig != null) {
                            String type = redisConfig.getType();
                            String url = redisConfig.getUrl();
                            String remark = redisConfig.getRemark();
                            //判断事件
                            if (type.equals("add")) {
                                System.out.println("监听到新增的配置，准备下载...");
                                //...连接ftp服务器，根据url找到相应的配置
                                Thread.sleep(500);
                                System.out.println("开始下载新的配置文件，下载路径为<" + url + ">");
                                //...下载配置到你指定的目录
                                Thread.sleep(1000);
                                System.out.println("下载成功，已经添加到项目中");
                                //...拷贝文件到项目目录
                            } else if (type.equals("update")) {
                                System.out.println("监听到新增的配置，准备下载...");
                                //...连接ftp服务器，根据url找到相应的配置
                                Thread.sleep(500);
                                System.out.println("开始下载新的配置文件，下载路径为<" + url + ">");
                                //...下载配置到你指定的目录
                                Thread.sleep(1000);
                                System.out.println("下载成功，已经添加到项目中");
                                System.out.println("删除项目中原配置文件...");
                                Thread.sleep(100);
                                //...删除源文件
                                System.out.println("拷贝配置文件到项目目录...");
                                //...拷贝文件到项目目录
                            } else if (type.equals("delete")) {
                                System.out.println("监听到需要删除配置");
                                System.out.println("删除项目中原配置文件...");
                            }

                            //TODO 视情况统一重启服务
                        }
                    }
                }
            }
        });
        countDownLatch.await();
        cto.closeZKClient();
    }
}
