package com.peach.zk.util;

import com.peach.zk.constant.Constant;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zengtao on 2016/8/16.
 */
public class ZkUtil {

    private static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

    //保证只要一个连接被创建，加一把锁
    private static final CountDownLatch latch = new CountDownLatch(1);
    public static synchronized ZooKeeper connectServer( ) {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (Exception e) {
            logger.error("", e);
        }
        return zk;
    }
}
