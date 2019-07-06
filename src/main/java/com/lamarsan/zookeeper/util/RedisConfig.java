package com.lamarsan.zookeeper.util;

/**
 * className: RedisConfig
 * description: TODO
 *
 * @author hasee
 * @version 1.0
 * @date 2019/7/6 21:27
 */
public class RedisConfig {
    private String type;
    private String url;
    private String remark;

    public RedisConfig() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
