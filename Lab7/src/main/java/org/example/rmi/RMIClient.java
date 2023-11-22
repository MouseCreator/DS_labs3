package org.example.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    public static void main(String[] args)  {
        String departmentsUrl = "//localhost:1234/Departments";
        try {
            DepartmentRemote departmentRemote = (DepartmentRemote) Naming.lookup(departmentsUrl);
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
