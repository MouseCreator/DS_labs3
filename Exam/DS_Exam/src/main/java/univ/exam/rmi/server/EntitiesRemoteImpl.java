package univ.exam.rmi.server;


import univ.exam.controller.CommonServerController;
import univ.exam.controller.initializer.ServerControllerInitializer;
import univ.exam.dto.PassableRequest;
import univ.exam.dto.PassableResponse;
import univ.exam.model.Letter;
import univ.exam.rmi.rinterface.EntitiesRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EntitiesRemoteImpl extends UnicastRemoteObject implements EntitiesRemote {

    private CommonServerController<Letter> serverController;

    public static EntitiesRemoteImpl instance() throws RemoteException {
        EntitiesRemoteImpl instance = new EntitiesRemoteImpl();
        instance.init(ServerControllerInitializer.getLetterServerController());
        return instance;
    }

    public void init(CommonServerController<Letter> serverController) {
        this.serverController = serverController;
    }
    protected EntitiesRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public PassableResponse get(PassableRequest request) throws RemoteException {
        return serverController.get(request);
    }
}
