package univ.exam.controller;

import univ.exam.dto.*;
import univ.exam.model.Letter;
import univ.exam.service.CrudService;
import univ.exam.util.Mapper;

public class LetterServiceController extends AbstractServerController<Letter> {

    public LetterServiceController(CrudService<Letter> service, FilterFactory<Letter> filterFactory) {
        super(service, filterFactory);
    }

    @Override
    public PassableResponse mapToResponse(Response<Letter> trainResponse) {
        return Mapper.fromTrainResponse(trainResponse);
    }
    @Override
    public Request<Letter> mapToRequest(PassableRequest request) {
        return Mapper.toTrainRequest(request);
    }
}
