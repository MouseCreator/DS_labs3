package univ.exam.socket.client;

import univ.exam.communicator.ClientCommunicator;
import univ.exam.dto.PassableResponse;
import univ.exam.dto.Request;
import univ.exam.dto.Response;
import univ.exam.model.Letter;
import univ.exam.util.Mapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketCommunicator implements ClientCommunicator<Letter> {

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ClientSocketCommunicator() {
    }
    public static ClientSocketCommunicator getInstance(Socket socket) {
        ClientSocketCommunicator communicator = new ClientSocketCommunicator();
        communicator.socket = socket;
        try {
            communicator.inputStream = new ObjectInputStream(socket.getInputStream());
            communicator.outputStream = new ObjectOutputStream(socket.getOutputStream());
            communicator.outputStream.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return communicator;
    }

    @Override
    public Response<Letter> sendAndReceive(Request<Letter> request) {
        try {
            outputStream.writeObject(Mapper.fromTrainRequest(request));
            PassableResponse response = (PassableResponse) inputStream.readObject();
            return Mapper.toTrainResponse(response);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() throws Exception{
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
