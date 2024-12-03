package com.pantrypro.core;

public interface Endpoint<R> {

    Object getResponse(R request) throws Exception;

}
