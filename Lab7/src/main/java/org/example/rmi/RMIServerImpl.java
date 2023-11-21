package org.example.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    protected RMIServerImpl() throws RemoteException {
    }
}
