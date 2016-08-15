package com.peach.zk.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by y400 on 2016/8/15.
 */
public class RemoteApiImpl extends UnicastRemoteObject implements RemoteApi {

    protected RemoteApiImpl() throws RemoteException {
    }

    public void saveName(String name) throws RemoteException {
        System.out.println("save name is " + name);
    }
}
