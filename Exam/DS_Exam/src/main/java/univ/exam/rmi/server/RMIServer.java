package univ.exam.rmi.server;


import univ.exam.rmi.rinterface.EntitiesRemote;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    private static final int port = 8888;

    public static void main(String[] args) throws RemoteException {
        EntitiesRemote departmentRemote = EntitiesRemoteImpl.instance();
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("Trains", departmentRemote);
    }
}
