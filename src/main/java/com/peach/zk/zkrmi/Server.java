package com.peach.zk.zkrmi;

import com.peach.zk.rmi.RemoteApi;
import com.peach.zk.rmi.RemoteApiImpl;

import java.util.Scanner;

/**
 * Created by zengtao on 2016/8/16.
 * 需要在客户端创建一个空的持久性节点
 * create /registry null
 */
public class Server {

    public static void main(String[] args) throws Exception {
        System.out.println("请输入地址和端口：");
        Scanner scanner = new Scanner(System.in);
        String host = scanner.next();
        int port = scanner.nextInt();
        ServiceProvider provider = new ServiceProvider();
        RemoteApi remoteApi = new RemoteApiImpl();
        provider.publish(remoteApi, host, port);
    }
}
