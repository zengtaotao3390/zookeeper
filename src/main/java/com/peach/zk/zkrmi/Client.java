package com.peach.zk.zkrmi;

import com.peach.zk.rmi.RemoteApi;

import java.rmi.RemoteException;

/**
 * Created by zengtao on 2016/8/16.
 */
public class Client {
    public static void main(String[] args) throws RemoteException, InterruptedException {
            ServiceConsumer consumer = new ServiceConsumer();
        while (true){
            consumer.watchNode();
            RemoteApi remoteApi = consumer.lookup();
            String retMsg = remoteApi.saveName("bigPeach");
            System.out.println(retMsg);
            Thread.sleep(100);
        }
    }
}
