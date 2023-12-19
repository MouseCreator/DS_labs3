package univ.exam.rmi.rinterface;


import univ.exam.dto.PassableRequest;
import univ.exam.dto.PassableResponse;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EntitiesRemote extends Remote {
    PassableResponse get(PassableRequest request) throws RemoteException;
}
