package org.example.rmi.client;

import org.example.communicator.ClientCommunicator;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.rmi.rinterface.DepartmentRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMICommunicator implements ClientCommunicator {
    private DepartmentRemote departmentRemote;
    public static RMICommunicator getInstance() {
        RMICommunicator communicator = new RMICommunicator();
        String departmentsUrl = "//localhost:1234/Departments";
        try {
            communicator.departmentRemote = (DepartmentRemote) Naming.lookup(departmentsUrl);
            return communicator;
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response sendAndReceive(Request request) {
        try {
            return departmentRemote.get(request);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() {

    }
}
