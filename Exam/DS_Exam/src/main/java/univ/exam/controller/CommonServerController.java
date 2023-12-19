package univ.exam.controller;

import univ.exam.dto.PassableRequest;
import univ.exam.dto.PassableResponse;
import univ.exam.model.Entity;


public interface CommonServerController<T extends Entity> {
    PassableResponse get(PassableRequest request);
}
