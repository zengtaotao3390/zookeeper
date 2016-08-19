package com.peach.zk.apacheLock;

import com.peach.zk.util.ZkUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zengtao on 2016/8/18.
 */
public class Driver {
    public static final Logger logger = LoggerFactory.getLogger(Driver.class);

    public static void main(String[] args) throws KeeperException, InterruptedException {
        while (true) {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            ZooKeeper zk = ZkUtil.connectServer(new CountDownLatch(1));
            logger.info("start");
            WriteLock writeLock = new WriteLock(zk, "/apache_lock", null, new LockListener() {
                @Override
                public void lockAcquired() {
                    logger.info("太开心了，我获得锁了");
                    countDownLatch.countDown();
                }

                @Override
                public void lockReleased() {
                    logger.error("锁已经释放了");
                }
            });
            writeLock.lock();
            countDownLatch.await();
            zk.close();
        }

    }
}
