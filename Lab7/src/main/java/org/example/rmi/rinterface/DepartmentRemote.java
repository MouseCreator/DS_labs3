package org.example.rmi.rinterface;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DepartmentRemote extends Remote {
    Response get(Request request) throws RemoteException;
}
