package com.peach.zk.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by y400 on 2016/8/15.
 */
public interface RemoteApi extends Remote {

    String saveName(String name) throws RemoteException;
}
