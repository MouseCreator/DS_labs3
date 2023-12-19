package univ.exam.controller;


import univ.exam.dto.*;
import univ.exam.model.Entity;
import univ.exam.service.CrudService;

public abstract class AbstractServerController<T extends Entity> implements CommonServerController<T>{
    private final CrudService<T> service;
    private final FilterFactory<T> filterFactory;
    public AbstractServerController(CrudService<T> service, FilterFactory<T> filterFactory) {
        this.service = service;
        this.filterFactory = filterFactory;
    }

    @Override
    public PassableResponse get(PassableRequest request) {
        Request<T> trainRequest = mapToRequest(request);
        Response<T> trainResponse = processRequest(trainRequest);
        return mapToResponse(trainResponse);
    }

    public abstract PassableResponse mapToResponse(Response<T> trainResponse);

    public abstract Request<T> mapToRequest(PassableRequest request);

    private Response<T> processRequest(Request<T> request) {
        int type = request.getType();
        try {
            return switch (type) {
                case RequestType.GET -> processGet(request);
                case RequestType.POST -> processPost(request);
                case RequestType.DELETE -> processDelete(request);
                case RequestType.UPDATE -> processUpdate(request);
                default -> Responses.error("Unknown request type " + type);
            };
        } catch (Exception e) {
            return Responses.error(e.getMessage());
        }

    }

    private Response<T> processUpdate(Request<T> request) {
        T updated = service.update(request.getBody());
        return Responses.of(updated);
    }

    private Response<T> processDelete(Request<T> request) {
        service.remove(Long.parseLong(request.getDetails()));
        return Responses.empty();
    }

    private Response<T> processPost(Request<T> request) {
        T train = service.add(request.getBody());
        return Responses.of(train);
    }

    private Response<T> processGet(Request<T> request) {
        String details = request.getDetails();
        if (details == null || details.isEmpty() || details.startsWith("all")) {
            return Responses.of(service.getAll());
        }
        CustomFilter<T> trainCustomFilter = filterFactory.toFilter(details);
        return Responses.of(service.getByFilter(trainCustomFilter));

    }
}
