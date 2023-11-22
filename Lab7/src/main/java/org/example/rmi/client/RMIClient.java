package org.example.rmi.client;

import org.example.model.dto.Request;
import org.example.rmi.rinterface.DepartmentRemote;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args)  {
        String departmentsUrl = "//localhost:1234/Departments";
        try {
            DepartmentRemote departmentRemote = (DepartmentRemote) Naming.lookup(departmentsUrl);
            Request request = new Request();
            request.setMethod("Get");
            request.setTarget("Department");
            request.setDetails("all");
            System.out.println(departmentRemote.get(request));
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
