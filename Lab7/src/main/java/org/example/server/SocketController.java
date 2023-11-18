package org.example.server;

import org.example.communicator.ServerCommunicator;
import org.example.controller.CommonController;
import org.example.model.dto.Request;
import org.example.model.dto.Response;
import org.example.model.dto.Status;

public class SocketController implements CommonController {
    private final ServerCommunicator communicator;
    public SocketController(ServerCommunicator serverCommunicator) {
        this.communicator = serverCommunicator;
    }

    private Response createResponse(String message) {
        Response response = new Response();
        response.setStatus(Status.QUESTION);
        response.setDetailsString(message);
        return response;
    }
    @Override
    public boolean askBoolean(String request) {
        Response response = createResponse(request);
        Boolean b;
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails();
            b = toBoolean(ans);
            if (b == null) {
                print("Boolean is expected!");
            } else {
                return b;
            }
        }

    }
    private String formatInput(String input) {
        return input.trim().toLowerCase();
    }
    private Boolean toBoolean(String s) {
        String b = formatInput(s);
        return switch (b) {
            case "y", "yes", "true", "t" -> true;
            case "n", "no", "false", "f" -> false;
            default -> null;
        };
    }

    @Override
    public int askInteger(String request) {
        Response response = createResponse(request);
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails().trim();
            try {
                return Integer.parseInt(ans);
            } catch (Exception e) {
                print("Integer is expected!");
            }
        }
    }

    @Override
    public long askLong(String request) {
        Response response = createResponse(request);
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails().trim();
            try {
                return Long.parseLong(ans);
            } catch (Exception e) {
                print("Long value is expected!");
            }
        }
    }

    @Override
    public String askString(String request) {
        Response response = createResponse(request);
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails();
            if (ans == null || ans.trim().isEmpty()){
                print("String value is expected!");
                continue;
            }
            return ans.trim();
        }
    }

    @Override
    public String askStringOr(String request, String defaultValue) {
        Response response = createResponse(request);
        communicator.send(response);
        Request answer = communicator.receive();
        String ans = answer.getDetails();
        if (ans == null || ans.trim().isEmpty()){
            return defaultValue;
        }
        return ans.trim();
    }

    @Override
    public int askIntegerOr(String request, int defaultValue) {
        Response response = createResponse(request);
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails().trim();
            if (ans.isEmpty())
                return defaultValue;
            try {
                return Integer.parseInt(ans);
            } catch (Exception e) {
                print("Integer is expected!");
            }
        }
    }

    @Override
    public long askLongOr(String request, long defaultValue) {
        Response response = createResponse(request);
        while (true) {
            communicator.send(response);
            Request answer = communicator.receive();
            String ans = answer.getDetails().trim();
            if (ans.isEmpty())
                return defaultValue;
            try {
                return Long.parseLong(ans);
            } catch (Exception e) {
                print("Long value is expected!");
            }
        }
    }

    @Override
    public void print(String s) {
        Response response = new Response();
        response.setStatus(Status.INFO);
        response.setDetailsString(s);
        communicator.send(response);
    }
}
