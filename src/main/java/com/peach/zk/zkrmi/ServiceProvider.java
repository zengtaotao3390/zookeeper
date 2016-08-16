package com.peach.zk.zkrmi;


import com.peach.zk.constant.Constant;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zengtao on 2016/8/16.
 */
public class ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProvider.class);
    //闭锁，用于等待事件的发生，可以用在异步操作
    private CountDownLatch latch = new CountDownLatch(1);

    public void publish(Remote remote, String host, int port) {
        String url = publishService(remote, host, port);
        if (url != null) {
            ZooKeeper zk = connectServer();
            if (zk != null) {
                createNode(zk, url);
            }
        }
    }

    private void createNode(ZooKeeper zk, String url) {
        byte[] data = url.getBytes();
        try {
            //创建一个临时性且有序的Znode
            String path = zk.create(Constant.ZK_PROVIDER_PATH, data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.debug("create zookeeper node({} => {})", path, url);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    private ZooKeeper connectServer() {
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

    private String publishService(Remote remote, String host, int port) {
        String url = null;
        try {
            url = String.format("rmi://%s:%s/%s", host, port, remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, remote);
            logger.debug("publish rmi service (url: {})", url);
        } catch (Exception e) {
            logger.error("", e);
        }
        return url;
    }
}
