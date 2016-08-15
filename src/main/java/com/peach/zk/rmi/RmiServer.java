package com.peach.zk.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by y400 on 2016/8/15.
 */
public class RmiServer {

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        int port = 1099;
        String url = "rmi://localhost:1099/RemoteApiImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url, new RemoteApiImpl());
    }
}
