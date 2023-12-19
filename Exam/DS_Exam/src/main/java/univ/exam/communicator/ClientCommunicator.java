package univ.exam.communicator;


import univ.exam.dto.Request;
import univ.exam.dto.Response;
import univ.exam.model.Entity;

public interface ClientCommunicator<T extends Entity> {
    Response<T> sendAndReceive(Request<T> request);

    void close() throws Exception;
}
