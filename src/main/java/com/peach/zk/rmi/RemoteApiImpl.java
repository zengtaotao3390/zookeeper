package com.peach.zk.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by y400 on 2016/8/15.
 */
public class RemoteApiImpl extends UnicastRemoteObject implements RemoteApi {

    public RemoteApiImpl() throws RemoteException {
    }

    public String saveName(String name) throws RemoteException {
        System.out.println("save name is " + name);
        return "save name is " + name;
    }
}
