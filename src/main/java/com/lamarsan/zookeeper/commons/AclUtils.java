package com.lamarsan.zookeeper.commons;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

/**
 * className: AclUtils
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 16:06
 */
public class AclUtils {
    public static String getDigestUserPwd(String id) throws Exception {
        return DigestAuthenticationProvider.generateDigest(id);
    }
}
