# zookeeper_demo

本demo是基于慕课网上Zookeeper分布式专题与dubbo微服务入门写的简单操作，会陆续更新

## 慕课网类

ZKConnect : 连接操作

ZKConnectSessionWatcher : 会话连接操作

ZKNodeOperator：关于结点的增删改查操作

Constants：静态常量

## 官方文档API类

Executor：主功能入口

DataMonitor：监听类

关于官方文档的类都写了详细的注释操作，类的主要目的是接受用户输入的系统命令，然后监控zookeeper的znode， 一旦znode存在，或者发生了变化，程序会把znode最新的数据存入文件， 然后起一个线程执行用户的命令，同时还会起两个线程输出执行结果及日志。
