package com.lamarsan.zookeeper.commons;

import org.apache.zookeeper.WatchedEvent;

/**
 * className: MyWatcher
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 20:34
 */
public class MyWatcher implements org.apache.zookeeper.Watcher {
    @Override
    public void process(WatchedEvent event) {
        System.out.println("触发watcher,结点路径为：" + event.getPath());
    }
}
