package com.peach.zk.apacheLock;

import com.peach.zk.util.ZkUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
                public void lockAcquired() throws InterruptedException {
                    logger.info("太开心了，我获得锁了");
                    countDownLatch.countDown();
                }

                @Override
                public void lockReleased() {
                    logger.error("锁已经释放了");
                }
            });
            writeLock.lock();
            //如果等待时间过少，当前连接已经删除了，而后面watch首节点变化，那么回调之后，会出现一个警告一次，告诉你连接已经关闭。
            //虽然这样做不影响正常的业务逻辑，但是也不好看，有不合理的地方，后面准备修改为不用session为key的Znode，而是使用固定的
            //然后使用一个连接不停的请求，不用每次都去建立一个连接，可以避免这样的问题发生。
            countDownLatch.await(6000, TimeUnit.MILLISECONDS);
            zk.close();
        }

    }
}
