package com.peach.zk.zkrmi;


import com.peach.zk.constant.Constant;
import com.peach.zk.util.ZkUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
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

    public void publish(Remote remote, String host, int port) {
        String url = publishService(remote, host, port);
        if (url != null) {
            ZooKeeper zk = ZkUtil.connectServer();
            if (zk != null) {
                createNode(zk, url);
            }
        }
    }

    private void createNode(ZooKeeper zk, String url) {
        byte[] data = url.getBytes();
        try {
            //创建一个临时性且有序的Znode
            long sessionid = zk.getSessionId();
            Stat stat = zk.exists(Constant.ZK_REGISTRY_PATH, false);
            if(stat == null){
                String parentPath = zk.create(Constant.ZK_REGISTRY_PATH, data,
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            String path = zk.create(Constant.ZK_PROVIDER_PATH, data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            logger.info("create zookeeper node({} => {})", path, url);
        } catch (Exception e) {
            logger.error("", e);
        }
    }



    private String publishService(Remote remote, String host, int port) {
        String url = null;
        try {
            url = String.format("rmi://%s:%s/%s", host, port, remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url, remote);
            logger.info("publish rmi service (url: {})", url);
        } catch (Exception e) {
            logger.error("", e);
        }
        return url;
    }
}
