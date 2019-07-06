package com.lamarsan.zookeeper.commons;

import org.apache.zookeeper.AsyncCallback;

/**
 * className: CreateCallBack
 * description: 异步创建结点
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 17:21
 */
public class CreateCallBack implements AsyncCallback.StringCallback {
    public void processResult(int rc, String path, Object ctx, String name) {
        System.out.println("创建结点：" + path);
        System.out.println((String) ctx);
    }
}
