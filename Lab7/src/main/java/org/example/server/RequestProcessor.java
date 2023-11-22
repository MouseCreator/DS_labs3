package org.example.server;

import org.example.model.dto.Request;
import org.example.model.dto.Response;

public interface RequestProcessor {
    Response get(Request request);
}
