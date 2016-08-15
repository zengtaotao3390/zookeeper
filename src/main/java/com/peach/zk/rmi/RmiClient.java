package com.peach.zk.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by y400 on 2016/8/15.
 */
public class RmiClient {

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        String url = "rmi://localhost:1099/RemoteApiImpl";
        RemoteApi remoteApi = (RemoteApi) Naming.lookup(url);
        remoteApi.saveName("peach");
    }
}
