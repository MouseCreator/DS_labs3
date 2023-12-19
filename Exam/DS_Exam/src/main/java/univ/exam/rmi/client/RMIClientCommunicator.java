package univ.exam.rmi.client;

import univ.exam.communicator.ClientCommunicator;
import univ.exam.dto.PassableRequest;
import univ.exam.dto.PassableResponse;
import univ.exam.dto.Request;
import univ.exam.dto.Response;
import univ.exam.model.Letter;
import univ.exam.rmi.rinterface.EntitiesRemote;
import univ.exam.util.Mapper;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClientCommunicator implements ClientCommunicator<Letter> {
    private EntitiesRemote entitiesRemote;

    private RMIClientCommunicator() {

    }
    public static RMIClientCommunicator getInstance() {
        RMIClientCommunicator communicator = new RMIClientCommunicator();
        String departmentsUrl = "//localhost:8888/Trains";
        try {
            communicator.entitiesRemote = (EntitiesRemote) Naming.lookup(departmentsUrl);
            return communicator;
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Response<Letter> sendAndReceive(Request<Letter> request) {
        try {
            PassableRequest passableRequest = Mapper.fromTrainRequest(request);
            PassableResponse passableResponse = entitiesRemote.get(passableRequest);
            return Mapper.toTrainResponse(passableResponse);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
