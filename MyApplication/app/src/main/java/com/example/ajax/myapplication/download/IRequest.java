package com.example.ajax.myapplication.download;

import com.example.ajax.myapplication.download.impl.request.Priority;

public interface IRequest {

    Priority getPriority();

    String getUri();
}
