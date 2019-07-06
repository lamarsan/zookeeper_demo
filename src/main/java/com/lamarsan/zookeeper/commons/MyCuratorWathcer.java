package com.lamarsan.zookeeper.commons;

import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * className: MyCuratorWathcer
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 20:33
 */
public class MyCuratorWathcer implements CuratorWatcher {
    @Override
    public void process(WatchedEvent watchedEvent) throws Exception {
        System.out.println("触发watcher,结点路径为：" + watchedEvent.getPath());
    }
}
