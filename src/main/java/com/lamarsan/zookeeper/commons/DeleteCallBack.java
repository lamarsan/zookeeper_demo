package com.lamarsan.zookeeper.commons;

import org.apache.zookeeper.AsyncCallback;

/**
 * className: DeleteCallBack
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/3 18:35
 */
public class DeleteCallBack implements AsyncCallback.VoidCallback {
    public void processResult(int rc, String path, Object ctx) {
        System.out.println("删除结点" + path);
        System.out.println((String)ctx);
    }
}
