package univ.exam.util;

import univ.exam.dto.*;
import univ.exam.model.Letter;

public class Mapper {
    public static LetterRequest toTrainRequest(PassableRequest request) {
        LetterRequest letterRequest = new LetterRequest();
        letterRequest.setBody(JsonUtil.deserialize(request.getBody(), Letter.class));
        letterRequest.setType(request.getType());
        letterRequest.setDetails(request.getDetails());
        return letterRequest;
    }

    public static PassableRequest fromTrainRequest(Request<Letter> request) {
        PassableRequest passableRequest = new PassableRequest();
        passableRequest.setBody(JsonUtil.serialize(request.getBody()));
        passableRequest.setType(request.getType());
        passableRequest.setDetails(request.getDetails());
        return passableRequest;
    }
    public static LetterResponse toTrainResponse(PassableResponse response) {
        LetterResponse letterResponse = new LetterResponse();
        for (String t : response.getEntities()) {
            letterResponse.add(JsonUtil.deserialize(t, Letter.class));
        }
        letterResponse.setDetails(response.getDetails());
        letterResponse.setStatus(response.getStatus());
        return letterResponse;
    }
    public static PassableResponse fromTrainResponse(Response<Letter> trainResponse) {
        PassableResponse passableResponse = new PassableResponse();
        for (Letter t : trainResponse.getAll()) {
            passableResponse.getEntities().add(JsonUtil.serialize(t));
        }
        passableResponse.setDetails(trainResponse.getDetails());
        passableResponse.setStatus(trainResponse.getStatus());
        return passableResponse;
    }
}
